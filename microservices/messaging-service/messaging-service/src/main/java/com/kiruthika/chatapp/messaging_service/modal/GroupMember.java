package com.kiruthika.chatapp.messaging_service.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupMember {

    @Id
    private GroupUserId id;

    private LocalDateTime joinedAt;


}
