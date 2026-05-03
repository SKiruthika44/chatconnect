package com.kiruthika.chatapp.messaging_service.event;


import com.kiruthika.chatapp.messaging_service.dto.GroupMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMessageDeletedForEveryoneEvent {
    private GroupMessageResponseDto groupMessageResponseDto;
}
