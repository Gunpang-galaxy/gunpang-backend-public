package com.galaxy.gunpang.avatar.service;

import com.galaxy.gunpang.avatar.model.dto.AvatarGatchaResDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarAddReqDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarResDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarWatchResDto;

public interface AvatarService {
    public void addRandomAvatar(Long userId, AvatarAddReqDto avatarAddReqDto);
    public void addWithBefore(Long userId, AvatarAddReqDto avatarAddReqDto, int n);
    public AvatarResDto getCurAvatarResDto(Long userId);
    public AvatarWatchResDto getCurAvatarWatchResDto(Long userId);
    public AvatarResDto getAvatarResDto(Long avatarId, Long userId);
}
