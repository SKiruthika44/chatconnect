package com.kiruthika.chatapp.messaging_service.event;

import com.kiruthika.chatapp.messaging_service.dto.GroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCreatedEvent {
    private GroupResponseDto groupResponseDto;
}
