package com.galaxy.gunpang.avatar.service;

import com.galaxy.gunpang.avatar.exception.AvatarNotFoundException;
import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.AvatarType;
import com.galaxy.gunpang.avatar.model.dto.AvatarGatchaResDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarNamingReqDto;
import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.AvatarTypeRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService{

    private final AvatarRepository avatarRepository;
    private final AvatarTypeRepository avatarTypeRepository;
    private final DeathCauseRepository deathCauseRepository;
    private final UserRepository userRepository;

    @Override
    public AvatarGatchaResDto addRandomAvatar(Long userId) {
        User user = userRepository.getReferenceById(userId);
        AvatarType randomAvatarType = avatarTypeRepository.getAvatarTypeByRandom();
        Avatar avatar = Avatar.builder()
                .avatarType(randomAvatarType)
                .user(user)
                .name(randomAvatarType.getDefaultName())
                .healthPoint((byte) 10)
                .stage(Stage.LAND)
                .status(Status.ALIVE)
                .startedDate(LocalDate.now())
                .build();
        Avatar savedAvatar = avatarRepository.save(avatar);

        return AvatarGatchaResDto.builder()
                .avatarId(savedAvatar.getId())
                .defaultName(savedAvatar.getName())
                .defaultImg(savedAvatar.getAvatarType().getDefaultImg())
                .build();
    }

    @Override
    public void namingAvatar(AvatarNamingReqDto avatarNamingReqDto) {
        Avatar avatar = avatarRepository.findById(avatarNamingReqDto.getAvatarId()).orElseThrow(
                () -> new AvatarNotFoundException(avatarNamingReqDto.getAvatarId())
        );
        avatar.setName(avatarNamingReqDto.getName());
        avatarRepository.save(avatar);
    }
}
