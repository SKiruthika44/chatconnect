package com.kiruthika.chatapp.ws_service.event;

import com.kiruthika.chatapp.ws_service.dto.GroupMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageStatusUpdatedEvent {
    private GroupMessageResponseDto groupMessageResponseDto;
}
