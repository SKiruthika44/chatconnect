package com.kiruthika.chatapp.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder

public class UserResponseDTO {
    private long id;

    private String username;

    private LocalDateTime lastSeen;

    private String profileImage;

    private String preferredLanguage;
}
