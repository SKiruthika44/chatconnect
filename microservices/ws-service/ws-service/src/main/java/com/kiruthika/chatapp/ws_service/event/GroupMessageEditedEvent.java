package com.kiruthika.chatapp.ws_service.event;


import com.kiruthika.chatapp.ws_service.dto.GroupMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMessageEditedEvent {
    private GroupMessageResponseDto dto;
}
