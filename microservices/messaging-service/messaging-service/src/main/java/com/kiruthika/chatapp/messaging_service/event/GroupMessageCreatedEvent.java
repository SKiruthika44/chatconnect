package com.kiruthika.chatapp.messaging_service.event;


import com.kiruthika.chatapp.messaging_service.dto.GroupMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageCreatedEvent {
    private GroupMessageResponseDto responseDto;
    private Long groupId;
}
