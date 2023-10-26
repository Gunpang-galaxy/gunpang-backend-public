package com.galaxy.gunpang.avatar.service;

import com.galaxy.gunpang.avatar.model.dto.AvatarGatchaResDto;
import com.galaxy.gunpang.avatar.model.dto.AvatarNamingReqDto;

public interface AvatarService {
    public AvatarGatchaResDto addRandomAvatar(Long userId);
    public void namingAvatar(AvatarNamingReqDto avatarNamingReqDto);
}
