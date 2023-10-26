package com.galaxy.gunpang.avatar.service;

import com.galaxy.gunpang.avatar.exception.AvatarAlreadyExistException;
import com.galaxy.gunpang.avatar.exception.AvatarNotFoundException;
import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.AvatarType;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.dto.*;
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
        if(avatarRepository.existsByUser_IdAndStatus(userId, Status.ALIVE)) throw new AvatarAlreadyExistException(userId);
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

    @Override
    public AvatarResDto getCurAvatarResDto(Long userId) {
        Long curAvatarId = avatarRepository.getCurIdByUserId(userId).orElseThrow(
                () -> new AvatarNotFoundException(userId)
        );

        return getAvatarResDto(curAvatarId, userId);
    }

    @Override
    public AvatarResDto getAvatarResDto(Long avatarId, Long userId) {
        Avatar avatar = avatarRepository.findByIdAndUser_Id(avatarId, userId).orElseThrow(
                () -> new AvatarNotFoundException(userId)
        );
        Long prev = avatarRepository.getPrevIdByIdAndUserId(avatarId, userId).orElse(-1L);
        Long next = avatarRepository.getNextIdByIdAndUserId(avatarId, userId).orElse(-1L);
        switch (avatar.getStatus()){
            case ALIVE -> {
                return AvatarAliveResDto.builder()
                        .avatarType(avatar.getAvatarType())
                        .name(avatar.getName())
                        .stage(avatar.getStage())
                        .status(avatar.getStatus())
                        .healthPoint(avatar.getHealthPoint())
                        .startedDate(avatar.getStartedDate())
                        .prev(prev)
                        .next(next)
                        .build();
            }
            case DEAD -> {
                DeathCause deathCause = deathCauseRepository.findFirstByAvatar_IdOrderByIdDesc(avatar.getId()).orElseThrow(

                );
                return AvatarDeadResDto.builder()
                        .avatarType(avatar.getAvatarType())
                        .name(avatar.getName())
                        .status(avatar.getStatus())
                        .startedDate(avatar.getStartedDate())
                        .prev(prev)
                        .next(next)
                        .finishedDate(avatar.getFinishedDate())
                        .deathCause(deathCause)
                        .build();
            }
            case GRADUATED -> {
                return AvatarGradResDto.builder()
                        .avatarType(avatar.getAvatarType())
                        .name(avatar.getName())
                        .stage(avatar.getStage())
                        .status(avatar.getStatus())
                        .startedDate(avatar.getStartedDate())
                        .prev(prev)
                        .next(next)
                        .finishedDate(avatar.getFinishedDate())
                        .build();
            }
        }

        return null;
    }
}
