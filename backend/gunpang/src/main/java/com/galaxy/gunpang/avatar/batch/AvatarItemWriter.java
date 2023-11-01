//package com.galaxy.gunpang.avatar.batch;
//
//import com.galaxy.gunpang.avatar.model.Avatar;
//import com.galaxy.gunpang.avatar.model.DeathCause;
//import com.galaxy.gunpang.avatar.model.enums.Cause;
//import com.galaxy.gunpang.avatar.model.enums.Status;
//import com.galaxy.gunpang.avatar.repository.AvatarRepository;
//import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class AvatarItemWriter implements ItemWriter<Avatar> {
//    private final AvatarRepository avatarRepository;
//    private final DeathCauseRepository deathCauseRepository;
//
//    @Override
//    public void write(List<? extends Avatar> avatars) {
//        for(Avatar avatar: avatars){
//            if(avatar.getHealthPoint() == 0){
//                DeathCause deathCause = DeathCause.builder()
//                        .cause(Cause.SLEEP_BROKEN)
//                        .avatar(avatar)
//                        .build();
//                deathCauseRepository.save(deathCause);
//                avatar.setDeathCause(deathCause);
//                avatar.setStatus(Status.DEAD);
//                avatar.setFinishedDate(LocalDate.now());
//            }
//            avatarRepository.save(avatar);
//        }
//    }
//}
