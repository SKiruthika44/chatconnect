package com.chatConnect.backend.Modal;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MessageUserId implements Serializable {

    private long messageId;
    private long userId;

    public MessageUserId(long messageId, long userId) {


        this.messageId = messageId;
        this.userId = userId;
    }

    public MessageUserId() {
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
