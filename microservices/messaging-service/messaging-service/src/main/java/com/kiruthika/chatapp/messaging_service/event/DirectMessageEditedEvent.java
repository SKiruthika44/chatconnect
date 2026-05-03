package com.kiruthika.chatapp.messaging_service.event;

import com.kiruthika.chatapp.messaging_service.dto.DirectMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class DirectMessageEditedEvent {
    private DirectMessageResponseDto directMessageResponseDto;
}
