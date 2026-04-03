package com.chatConnect.backend.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
public class MessageReaction {

    @Id

    private MessageUserId id;
    private LocalDateTime reactedAt;

    private String emoji;

    public MessageReaction(MessageUserId id,String emoji) {
        this.id = id;
        this.emoji=emoji;
        this.reactedAt=LocalDateTime.now();
    }

    public MessageReaction() {
    }

    public MessageUserId getId() {
        return id;
    }

    public void setId(MessageUserId id) {
        this.id = id;
    }

    public LocalDateTime getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(LocalDateTime reactedAt) {
        this.reactedAt = reactedAt;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
