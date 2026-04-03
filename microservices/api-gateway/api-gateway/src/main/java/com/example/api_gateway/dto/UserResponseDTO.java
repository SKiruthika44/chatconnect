package com.example.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor


public class UserResponseDTO {
    private long id;

    private String username;

    private LocalDateTime lastSeen;

    private String profileImage;

    private String preferredLanguage;

}
