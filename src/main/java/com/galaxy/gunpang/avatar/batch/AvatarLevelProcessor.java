package com.galaxy.gunpang.avatar.batch;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AvatarLevelProcessor implements ItemProcessor<Avatar,Avatar> {
    @Override
    public Avatar process(Avatar avatar) {
        if(avatar.getStatus() != Status.ALIVE
                || !avatar.getStartedDate().isBefore(LocalDate.now())
                || ChronoUnit.DAYS.between(avatar.getStartedDate(), LocalDate.now()) % 7 != 0) return avatar;
        Stage[] stages = Stage.values();
        int level = avatar.getStage().ordinal();
        log.debug("stage length = {}", stages.length);
        log.debug("level = {}", level);
        if(level < stages.length - 1) {
            log.debug("id = {} is level up", avatar.getId());
            avatar.setStage(stages[level + 1]);
            avatar.setHealthPoint((byte) 10);
        } else if(level == stages.length - 1){
            log.debug("id = {} is graduate", avatar.getId());
            avatar.setStatus(Status.GRADUATED);
            avatar.setFinishedDate(LocalDate.now());
            avatar.setHealthPoint((byte) 10);
        }
        return avatar;
    }
}
