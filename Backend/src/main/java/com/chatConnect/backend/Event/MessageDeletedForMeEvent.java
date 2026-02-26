package com.chatConnect.backend.Event;

public class MessageDeletedForMeEvent {
    private long msgId;
    private String username;

    public MessageDeletedForMeEvent(long msgId, String username) {
        this.msgId = msgId;
        this.username=username;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
