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
    public Avatar process(Avatar avatar){
        if(avatar.getStatus() != Status.ALIVE) return avatar;
        log.debug("avatarId : " + avatar.getId());
        avatarProcessor.init(avatar);
        log.debug("init 완료");

        avatar = avatarProcessor.exerciseProcess(avatar);
        log.debug("exercise 완료 | id = " + avatar.getId() + " | hp :  "+ avatar.getHealthPoint());
        if(avatar.getStatus() == Status.DEAD) return avatar;
        avatar = avatarProcessor.sleepProcess(avatar);
        log.debug("sleep 완료 | id = "+ avatar.getId() + " | hp :  " + avatar.getHealthPoint());
        if(avatar.getStatus() == Status.DEAD) return avatar;
        avatar = avatarProcessor.foodProcess(avatar);
        log.debug("food 완료 | id = "+ avatar.getId() + " | hp :  " + avatar.getHealthPoint());

        return avatar;
    }
}
