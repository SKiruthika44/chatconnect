package com.chatConnect.backend.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@Entity

public class MessageRead {
    @Id
    private MessageUserId id;
    private LocalDateTime readAt;

    public MessageRead(MessageUserId id) {
        this.id = id;
    }

    public MessageRead() {
    }

    public MessageUserId getId() {
        return id;
    }

    public void setId(MessageUserId id) {
        this.id = id;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }


    @PrePersist

    public void setReadAt() {
        this.readAt = LocalDateTime.now();
    }


}
