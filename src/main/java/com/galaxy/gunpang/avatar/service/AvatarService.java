package com.galaxy.gunpang.avatar.service;

import com.galaxy.gunpang.avatar.model.dto.AvatarAddReqDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarResDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarWatchResDto;

public interface AvatarService {
    void addRandomAvatar(Long userId, AvatarAddReqDto avatarAddReqDto);
    void addWithBefore(Long userId, AvatarAddReqDto avatarAddReqDto, int n);
    AvatarResDto getCurAvatarResDto(Long userId);
    AvatarWatchResDto getCurAvatarWatchResDto(Long userId);
    AvatarResDto getAvatarResDto(Long avatarId, Long userId);

    void authenticate(Long avatarId, Long userId);
}
