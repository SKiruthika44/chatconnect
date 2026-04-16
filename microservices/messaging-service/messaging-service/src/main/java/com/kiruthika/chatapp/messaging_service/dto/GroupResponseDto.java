package com.kiruthika.chatapp.messaging_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponseDto {
    private long id;

    private String groupName;

    private String admin;

    private List<String> groupMembers;

    private LocalDateTime createdAt;
}
