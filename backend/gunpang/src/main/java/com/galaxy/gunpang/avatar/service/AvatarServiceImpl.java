package com.galaxy.gunpang.avatar.service;

import com.galaxy.gunpang.avatar.exception.AvatarAlreadyExistException;
import com.galaxy.gunpang.avatar.exception.AvatarNotFoundException;
import com.galaxy.gunpang.avatar.exception.DeathCauseNotFoundException;
import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.dto.*;
import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordResDto;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.dailyRecord.service.DailyRecordService;
import com.galaxy.gunpang.goal.exception.GoalNotFoundException;
import com.galaxy.gunpang.goal.model.Goal;
import com.galaxy.gunpang.goal.model.dto.GoalResDto;
import com.galaxy.gunpang.goal.repository.GoalRepository;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService{

    private final AvatarRepository avatarRepository;
    private final DailyRecordRepository dailyRecordRepository;
    private final DailyRecordService dailyRecordService;
//    private final AvatarTypeRepository avatarTypeRepository;
    private final GoalRepository goalRepository;
    private final DeathCauseRepository deathCauseRepository;
    private final UserRepository userRepository;

    @Override
    public void addRandomAvatar(Long userId, AvatarAddReqDto avatarAddReqDto) {
        User user = userRepository.getReferenceById(userId);
        if(avatarRepository.existsByUser_IdAndStatus(userId, Status.ALIVE)) throw new AvatarAlreadyExistException(userId);
        Avatar avatar = Avatar.builder()
                .avatarType(avatarAddReqDto.getAvatarType())
                .user(user)
                .name(avatarAddReqDto.getName())
                .healthPoint((byte) 10)
                .stage(Stage.LAND)
                .status(Status.ALIVE)
                .startedDate(LocalDate.now())
                .build();
        avatarRepository.save(avatar);
//        return AvatarGatchaResDto.builder()
//                .avatarId(savedAvatar.getId())
//                .defaultName(savedAvatar.getName())
//                .defaultImg(savedAvatar.getAvatarType().getDefaultImg())
//                .build();
    }

//    @Override
//    public void namingAvatar(AvatarAddReqDto avatarAddReqDto) {
//        Avatar avatar = avatarRepository.findById(avatarAddReqDto.getAvatarId()).orElseThrow(
//                () -> new AvatarNotFoundException(avatarAddReqDto.getAvatarId())
//        );
//        avatar.setName(avatarAddReqDto.getName());
//        avatarRepository.save(avatar);
//    }

    @Override
    public AvatarResDto getCurAvatarResDto(Long userId) {
        Long curAvatarId = avatarRepository.getCurIdByUserId(userId).orElseThrow(
                () -> new AvatarNotFoundException(userId)
        );

        return getAvatarResDto(curAvatarId, userId);
    }

    @Override
    public AvatarWatchResDto getCurAvatarWatchResDto(Long userId) {
        Long curAvatarId = avatarRepository.getCurIdByUserId(userId).orElseThrow(
                () -> new AvatarNotFoundException(userId)
        );
        Avatar avatar = avatarRepository.findById(curAvatarId).get();
        return AvatarWatchResDto.builder().avatarType(avatar.getAvatarType())
                .healthPoint(avatar.getHealthPoint()/10f)
                .status(avatar.getStatus())
                .stage(avatar.getStage())
                .build();
    }

    @Override
    public AvatarResDto getAvatarResDto(Long avatarId, Long userId) {
        Avatar avatar = avatarRepository.findByIdAndUser_Id(avatarId, userId).orElseThrow(
                () -> new AvatarNotFoundException(userId)
        );
        Long prev = avatarRepository.getPrevIdByIdAndUserId(avatarId, userId).orElse(-1L);
        Long next = avatarRepository.getNextIdByIdAndUserId(avatarId, userId).orElse(-1L);
        Goal goal = goalRepository.findByAvatar_Id(avatar.getId()).orElseThrow(
                () -> new GoalNotFoundException(avatar.getId())
        );
        GoalResDto goalResDto = GoalResDto.builder()
                .exerciseDay(goal.getExerciseDay())
                .exerciseTime(goal.getExerciseTime())
                .sleepStart(goal.getSleepStartTime().toString())
                .sleepEnd(goal.getSleepEndTime().toString())
                .build();

        AvatarResDto avatarResDto = AvatarResDto.builder()
                .avatarType(avatar.getAvatarType())
                .name(avatar.getName())
                .stage(avatar.getStage())
                .status(avatar.getStatus())
                .healthPoint(avatar.getHealthPoint() / 10f)
                .startedDate(avatar.getStartedDate().toString())
                .finishedDate(avatar.getFinishedDate()==null?null:avatar.getFinishedDate().toString())
                .goal(goalResDto)
                .prev(prev)
                .next(next)
                .build();

        switch (avatar.getStatus()){
            case ALIVE -> {
//                DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, LocalDate.now()).orElse(null);
                CheckDailyRecordResDto checkDailyRecordResDto = dailyRecordService.checkDailyRecord(userId, LocalDate.now().toString());
                avatarResDto.setContents(checkDailyRecordResDto);
//                if(dailyRecord != null) {
//                    avatarResDto.setContents(CheckDailyRecordResDto.builder()
//                            .exerciseTime(dailyRecord.getExerciseAccTime().toString())
//                            .awakeAt(dailyRecord.getAwakeAt().toString())
//                            .sleepAt(dailyRecord.getSleepAt().toString())
//                            .breakfastFoodType(dailyRecord.getBreakfastFoodType())
//                            .lunchFoodType(dailyRecord.getLunchFoodType())
//                            .dinnerFoodType(dailyRecord.getDinnerFoodType())
//                            .build());
//                }else{
//                    avatarResDto.setContents(new CheckDailyRecordResDto());
//                }
            }
            case DEAD -> {
                DeathCause deathCause = deathCauseRepository.findFirstByAvatar_IdOrderByIdDesc(avatar.getId()).orElseThrow(
                        ()-> new DeathCauseNotFoundException(avatar.getId())
                );

                avatarResDto.setContents(AvatarDeadResDto.builder()
                        .deathCause(deathCause.getCause().name())
                        .build());
            }
            case GRADUATED -> {
                int cnt = 0;
                for(int i = 0; i < 7; i++){
                    if((goal.getExerciseDay() & (1<<i))!=0) ++cnt;
                }

                AvatarGradResDto avatarGradResDto = AvatarGradResDto.builder()
                        .exerciseTotal(cnt * 4)
                        .foodTotal(28)
                        .sleepTotal(28)
                        .exerciseSuccessCnt(cnt * 4)
                        .foodSuccessCnt(28)
                        .sleepSuccessCnt(28)
                        .build();

                deathCauseRepository.findAllByAvatar_Id(avatar.getId()).stream()
                                .forEach(deathCause -> {
                                    switch (deathCause.getCause()){
                                        case EXERCISE_LACK -> avatarGradResDto.setExerciseSuccessCnt(avatarGradResDto.getExerciseSuccessCnt() - 1);
                                        case FOOD_LACK -> avatarGradResDto.setFoodSuccessCnt(avatarGradResDto.getFoodSuccessCnt() - 1);
                                        case SLEEP_BROKEN -> avatarGradResDto.setSleepSuccessCnt(avatarGradResDto.getSleepSuccessCnt() - 1);
                                    }
                                });
                avatarResDto.setContents(avatarGradResDto);
            }
        }

        return avatarResDto;
    }
}
