package com.chatConnect.backend.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@Entity

public class PrivateMessageEmojiReaction {

    @Id
    private MessageUserId id;
    private String emoji;
    private LocalDateTime createdAt;

    public PrivateMessageEmojiReaction(MessageUserId id, String emoji) {
        this.id = id;
        this.emoji = emoji;

    }

    public PrivateMessageEmojiReaction() {
    }

    public MessageUserId getId() {
        return id;
    }

    public void setId(MessageUserId id) {
        this.id = id;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
