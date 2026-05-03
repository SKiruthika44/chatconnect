package com.kiruthika.chatapp.messaging_service.event;

import com.kiruthika.chatapp.messaging_service.dto.GroupMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMessageEmojiCreatedEvent {
    private GroupMessageResponseDto groupMessageResponseDto;
}
