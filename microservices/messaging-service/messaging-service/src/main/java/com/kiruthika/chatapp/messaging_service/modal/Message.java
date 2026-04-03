package com.kiruthika.chatapp.messaging_service.modal;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="message_type")
@Data

@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private long msgId;


    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;
    private boolean deletedForEveryone=false;

    private LocalDateTime deletedAt;

    private String detectedLanguage;



    private Long senderId;



}
