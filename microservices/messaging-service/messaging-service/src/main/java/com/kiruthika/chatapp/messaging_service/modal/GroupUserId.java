package com.kiruthika.chatapp.messaging_service.modal;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class GroupUserId implements Serializable {
    private Long groupId;
    private Long userId;

}
