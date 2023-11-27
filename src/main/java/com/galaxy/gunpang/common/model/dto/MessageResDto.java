package com.galaxy.gunpang.common.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResDto {
    private String message;

    public static MessageResDto msg(String message){
        return MessageResDto.builder().message(message).build();
    }
}
