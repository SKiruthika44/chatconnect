package com.chatConnect.backend.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity

public class MessageDeletion {

    @Id
    private MessageUserId id;

    private LocalDateTime deletedAt;

    public MessageDeletion(MessageUserId id) {
        this.id = id;
        this.deletedAt=LocalDateTime.now();
    }

    public MessageDeletion() {
    }

    public MessageUserId getId() {
        return id;
    }

    public void setId(MessageUserId id) {
        this.id = id;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
