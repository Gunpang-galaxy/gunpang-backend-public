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

    public boolean checkDailyRecord(Avatar avatar, int damage, Cause cause){
        if(!this.goal.isPresent()) return false;
        if(!dailyRecord.isPresent() && this.goal.isPresent()){
            damaged(avatar, damage);
            createDeathCause(avatar, cause);
            log.debug("미기록으로 체력 깎임 : {}, {}", avatar.getId(), cause);
            return false;
        }
        return true;
    }
    public Avatar exerciseProcess(Avatar avatar){
        if(!checkDailyRecord(avatar, 1, Cause.EXERCISE_LACK)) return avatar;
        // 일 : 6, 월 : 5, 화 : 4, ... , 토 : 0
        int todayOfWeek = 6 - (now.getDayOfWeek().getValue() % 7);
        byte exerciseDay = goal.get().getExerciseDay();

        if((exerciseDay & (1 << todayOfWeek)) != 0 && dailyRecord.get().getExerciseAccTime() < goal.get().getExerciseTime()){
            damaged(avatar, 1);
            createDeathCause(avatar, Cause.EXERCISE_LACK);
            log.debug("운동으로 체력 깎임 : {}", avatar.getId());
        }
        return avatar;
    }

    public Avatar sleepProcess(Avatar avatar){
        if(!checkDailyRecord(avatar, 1, Cause.SLEEP_BROKEN)) return avatar;
        LocalDateTime sleepStartTime =  LocalDateTime.of(now, goal.get().getSleepStartTime());
        LocalDateTime sleepEndTime = LocalDateTime.of(now, goal.get().getSleepEndTime());
        if(!sleepStartTime.isBefore(sleepEndTime)) sleepStartTime.minusDays(1);
        Duration gap = Duration.between(sleepStartTime, sleepEndTime);

        LocalDateTime sleepAt = dailyRecord.get().getSleepAt();
        LocalDateTime awakeAt = dailyRecord.get().getAwakeAt();
        Duration overlap = Duration.ZERO;
        if(sleepAt != null && awakeAt != null)
            overlap = Duration.between(sleepAt.isBefore(sleepStartTime)?sleepStartTime:sleepAt
                , awakeAt.isAfter(sleepEndTime)?sleepEndTime:awakeAt);

        if(((double)overlap.toMinutes() / gap.toMinutes()) < 0.9){
            damaged(avatar, 1);
            createDeathCause(avatar, Cause.SLEEP_BROKEN);
            log.debug("수면으로 체력 깎임 : {}", avatar.getId());
        }
        return avatar;
    }

    public Avatar foodProcess(Avatar avatar){
        if(!checkDailyRecord(avatar, 1, Cause.FOOD_LACK)) return avatar;
        int mealCnt = 0;
        int unHealthCnt = 0;

        if(dailyRecord.get().getBreakfastFoodType() == null || dailyRecord.get().getBreakfastFoodType() == FoodType.NOT_RECORD){
            ++mealCnt;
            ++unHealthCnt;
        }else if(dailyRecord.get().getBreakfastFoodType() == FoodType.BAD) ++unHealthCnt;
        if(dailyRecord.get().getLunchFoodType() == null || dailyRecord.get().getLunchFoodType() == FoodType.NOT_RECORD){
            ++mealCnt;
            ++unHealthCnt;
        }else if(dailyRecord.get().getLunchFoodType() == FoodType.BAD) ++unHealthCnt;
        if(dailyRecord.get().getDinnerFoodType() == null || dailyRecord.get().getDinnerFoodType() == FoodType.NOT_RECORD){
            ++mealCnt;
            ++unHealthCnt;
        }else if(dailyRecord.get().getDinnerFoodType() == FoodType.BAD) ++unHealthCnt;

        if(mealCnt < 3 || unHealthCnt > 1){
            damaged(avatar, 1);
            createDeathCause(avatar, Cause.FOOD_LACK);
            log.debug("식사로 체력 깎임 : {}", avatar.getId());
        }
        return avatar;
    }

    private void damaged(Avatar avatar, int damage){
        byte hp = avatar.getHealthPoint();
        hp = (byte)Math.max(0,hp - damage);
        avatar.setHealthPoint(hp);
    }

    private void createDeathCause(Avatar avatar, Cause cause){
        DeathCause deathCause = DeathCause.builder().avatar(avatar).cause(cause).date(now).build();
        deathCauseRepository.save(deathCause);
        if(avatar.getHealthPoint() <= 0) {
            avatar.setStatus(Status.DEAD);
            avatar.setFinishedDate(LocalDate.now());
        }
    }
}
