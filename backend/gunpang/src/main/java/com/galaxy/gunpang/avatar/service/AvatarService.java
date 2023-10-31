package com.galaxy.gunpang.avatar.service;

import com.galaxy.gunpang.avatar.model.dto.AvatarGatchaResDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarNamingReqDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarResDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarWatchResDto;

public interface AvatarService {
    public AvatarGatchaResDto addRandomAvatar(Long userId);
    public void namingAvatar(AvatarNamingReqDto avatarNamingReqDto);
    public AvatarResDto getCurAvatarResDto(Long userId);
    public AvatarWatchResDto getCurAvatarWatchResDto(Long userId);
    public AvatarResDto getAvatarResDto(Long avatarId, Long userId);
}
