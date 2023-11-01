//package com.galaxy.gunpang.avatar.batch;
//
//import com.galaxy.gunpang.avatar.model.Avatar;
//import com.galaxy.gunpang.avatar.model.DeathCause;
//import com.galaxy.gunpang.avatar.model.enums.Cause;
//import com.galaxy.gunpang.avatar.model.enums.Status;
//import com.galaxy.gunpang.avatar.repository.AvatarRepository;
//import com.galaxy.gunpang.avatar.service.AvatarService;
//import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
//import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
//import com.galaxy.gunpang.dailyRecord.service.DailyRecordService;
//import com.galaxy.gunpang.goal.repository.GoalRepository;
//import com.galaxy.gunpang.user.model.User;
//import com.galaxy.gunpang.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.StepExecutionListener;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class AvatarExerciseTasklet implements Tasklet, StepExecutionListener {
//
//    private List<Avatar> avatars;
//
//    private final AvatarRepository avatarRepository;
//    private final UserRepository userRepository;
//
////    private final GoalRepository goalRepository;
//    private final DailyRecordRepository dailyRecordRepository;
////    private final UserRepository userRepository;
//
//    @Override
//    public void beforeStep(StepExecution stepExecution) {
//        // 나중에 user 쪽 findAllIds 구현하여 refactoring
////        avatars = avatarRepository.findAllAliveAvatar();
//    }
//
//    @Override
//    public ExitStatus afterStep(StepExecution stepExecution) {
//        return null;
//    }
//
//    @Override
//    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//        System.out.println("batch user test");
//        System.out.println("============================");
//        System.out.println(avatars.size());
//        avatars.stream().forEach(avatar -> {
//            System.out.println(avatar.getUser().getId());
//            Optional<DailyRecord> dr = dailyRecordRepository.getDailyRecordOnTodayByUserId(avatar.getUser().getId(), LocalDate.now());
//            if(!dr.isPresent()) {
//                byte hp = avatar.getHealthPoint();
//                hp = (byte)Math.max(0,hp - 3);
//                avatar.setHealthPoint(hp);
////                avatar.setDeathCause(DeathCause.builder().avatar(avatar).cause(Cause.SLEEP_BROKEN).build());
//                avatar.setStatus(Status.DEAD);
//                avatar.setFinishedDate(LocalDate.now());
//                avatarRepository.save(avatar);
//            }
//        });
//        return RepeatStatus.FINISHED;
//    }
//}
