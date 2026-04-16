package com.kiruthika.chatapp.ws_service.event;

import com.kiruthika.chatapp.ws_service.dto.GroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCreatedEvent {
    private GroupResponseDto groupResponseDto;
}
