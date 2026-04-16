package com.kiruthika.chatapp.ws_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GroupResponseDto {
    private long id;

    private String groupName;

    private String admin;

    private List<String> groupMembers;

    private LocalDateTime createdAt;

}
