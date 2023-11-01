package com.galaxy.gunpang.avatar.batch;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.enums.Cause;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.goal.model.Goal;
import com.galaxy.gunpang.goal.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AvatarProcessor {
    private final DeathCauseRepository deathCauseRepository;
    private final DailyRecordRepository dailyRecordRepository;
    private final GoalRepository goalRepository;
    private Optional<DailyRecord> dailyRecord;
    private Optional<Goal> goal;
    private LocalDate now;

    public void init(Avatar avatar){
        dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(avatar.getUser().getId(), LocalDate.now());
        goal = goalRepository.findByAvatar_Id(avatar.getId());
        now = LocalDate.now().minusDays(1);
    }

    public boolean necessaryPresent(){
        return this.dailyRecord.isPresent() && this.goal.isPresent();
    }

    public Avatar checkDailyRecord(Avatar avatar){
        if(!dailyRecord.isPresent() && this.goal.isPresent()){
            log.debug("미기록 체력 깎임");
            byte hp = avatar.getHealthPoint();
            hp = (byte)Math.max(0,hp - 3);
            avatar.setHealthPoint(hp);
            createDeathCause(avatar, Cause.SLEEP_BROKEN);
        }
//        log.debug("체력 안깎임");
        return avatar;
    }
    public Avatar exerciseProcess(Avatar avatar){
        int todayOfWeek = now.getDayOfWeek().getValue() - 1;
        byte exerciseDay = goal.get().getExerciseDay();

        if((exerciseDay & (1 << todayOfWeek)) != 0 && dailyRecord.get().getExerciseAccTime() < goal.get().getExerciseTime()){
            byte hp = avatar.getHealthPoint();
            hp = (byte)Math.max(0,hp - 1);
            avatar.setHealthPoint(hp);
            createDeathCause(avatar, Cause.EXERCISE_LACK);
            log.debug("운동으로 체력 깎임 : {}", avatar.getId());
        }
        return avatar;
    }

    public Avatar sleepProcess(Avatar avatar){
        LocalDateTime sleepStartTime =  LocalDateTime.of(now, goal.get().getSleepStartTime());
        LocalDateTime sleepEndTime = LocalDateTime.of(now, goal.get().getSleepEndTime());
        if(!sleepStartTime.isBefore(sleepEndTime)) sleepStartTime.minusDays(1);
        Duration gap = Duration.between(sleepStartTime, sleepEndTime);

        LocalDateTime sleepAt = dailyRecord.get().getSleepAt();
        LocalDateTime awakeAt = dailyRecord.get().getAwakeAt();
        Duration overlap = Duration.between(sleepAt.isBefore(sleepStartTime)?sleepStartTime:sleepAt
                , awakeAt.isAfter(sleepEndTime)?sleepEndTime:awakeAt);

        if(((double)overlap.toMinutes() / gap.toMinutes()) < 0.9){
            byte hp = avatar.getHealthPoint();
            hp = (byte)Math.max(0,hp - 1);
            avatar.setHealthPoint(hp);
            createDeathCause(avatar, Cause.SLEEP_BROKEN);
            log.debug(sleepStartTime.toString());
            log.debug(sleepEndTime.toString());
            log.debug(""+(sleepAt.isBefore(sleepStartTime)?sleepStartTime:sleepAt));
            log.debug(""+(awakeAt.isAfter(sleepEndTime)?sleepEndTime:awakeAt));
            log.debug(overlap.toMinutes() + " " + gap.toMinutes());
            log.debug(""+ (double)overlap.toMinutes() / gap.toMinutes());
            log.debug("수면으로 체력 깎임 : {}", avatar.getId());
        }
        return avatar;
    }

    public Avatar foodProcess(Avatar avatar){
        int mealCnt = 0;
        int unHealthCnt = 0;

        if(dailyRecord.get().getBreakfastFoodType() == FoodType.NOT_RECORD){
            ++mealCnt;
            ++unHealthCnt;
        }else if(dailyRecord.get().getBreakfastFoodType() == FoodType.BAD) ++unHealthCnt;
        if(dailyRecord.get().getLunchFoodType() == FoodType.NOT_RECORD){
            ++mealCnt;
            ++unHealthCnt;
        }else if(dailyRecord.get().getLunchFoodType() == FoodType.BAD) ++unHealthCnt;
        if(dailyRecord.get().getDinnerFoodType() == FoodType.NOT_RECORD){
            ++mealCnt;
            ++unHealthCnt;
        }else if(dailyRecord.get().getDinnerFoodType() == FoodType.BAD) ++unHealthCnt;

        if(mealCnt < 3 || unHealthCnt > 1){
            byte hp = avatar.getHealthPoint();
            hp = (byte)Math.max(0,hp - 1);
            avatar.setHealthPoint(hp);
            createDeathCause(avatar, Cause.FOOD_LACK);
            log.debug("식사로 체력 깎임 : {}", avatar.getId());
        }
        return avatar;
    }

    private void createDeathCause(Avatar avatar, Cause cause){
        if(avatar.getHealthPoint() > 0) return;
        DeathCause deathCause = DeathCause.builder().avatar(avatar).cause(cause).build();
        deathCauseRepository.save(deathCause);
        avatar.setDeathCause(deathCause);
        avatar.setStatus(Status.DEAD);
        avatar.setFinishedDate(LocalDate.now());
    }
}
