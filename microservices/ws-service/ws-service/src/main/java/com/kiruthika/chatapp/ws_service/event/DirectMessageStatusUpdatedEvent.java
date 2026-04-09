package com.kiruthika.chatapp.ws_service.event;

import com.kiruthika.chatapp.ws_service.dto.DirectMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectMessageStatusUpdatedEvent {
    private DirectMessageResponseDto directMessageResponseDto;
}
