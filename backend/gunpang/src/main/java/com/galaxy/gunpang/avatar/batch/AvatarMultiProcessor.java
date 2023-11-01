package com.galaxy.gunpang.avatar.batch;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvatarMultiProcessor implements ItemProcessor<Avatar,Avatar> {
    private final AvatarProcessor avatarProcessor;

    @Override
    public Avatar process(Avatar avatar) throws Exception {
        avatarProcessor.init(avatar);

        avatar = avatarProcessor.checkDailyRecord(avatar);
        if(avatar.getStatus() == Status.DEAD || !avatarProcessor.necessaryPresent()) return avatar;
        avatar = avatarProcessor.exerciseProcess(avatar);
        if(avatar.getStatus() == Status.DEAD) return avatar;
        avatar = avatarProcessor.sleepProcess(avatar);
        if(avatar.getStatus() == Status.DEAD) return avatar;
        avatar = avatarProcessor.foodProcess(avatar);

        return avatar;
    }
}
