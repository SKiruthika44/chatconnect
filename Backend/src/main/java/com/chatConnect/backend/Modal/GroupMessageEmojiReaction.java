package com.chatConnect.backend.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity

public class GroupMessageEmojiReaction {

    @Id
    private MessageUserId id;
    private String emoji;
    private LocalDateTime localDateTime;

    public GroupMessageEmojiReaction(MessageUserId id, String emoji) {
        this.id = id;
        this.emoji = emoji;

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

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public GroupMessageEmojiReaction() {
    }
}
