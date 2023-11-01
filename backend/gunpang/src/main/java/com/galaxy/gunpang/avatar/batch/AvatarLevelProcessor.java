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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AvatarLevelProcessor implements ItemProcessor<Avatar,Avatar> {
    @Override
    public Avatar process(Avatar avatar) throws Exception {
        Stage[] stages = Stage.values();
        int level = avatar.getStage().ordinal();
        if(level < stages.length - 1)
            avatar.setStage(stages[level+1]);
        else if(level == stages.length - 1){
            avatar.setStatus(Status.GRADUATED);
            avatar.setFinishedDate(LocalDate.now());
        }
        return avatar;
    }
}
