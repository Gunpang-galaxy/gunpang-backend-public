package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.AvatarType;
import com.galaxy.gunpang.avatar.model.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public abstract class AvatarResDto {
    private String avatarType;
    private String name;
    private String status;
    private String startedDate;
    private long prev;
    private long next;


    public AvatarResDto(AvatarType avatarType, String name, Status status, LocalDate startedDate, long prev, long next){
        this.avatarType = avatarType.getDefaultImg();
        this.name = name;
        this.status = status.name();
        this.startedDate = startedDate.toString();
        this.prev = prev;
        this.next = next;
    }
}
