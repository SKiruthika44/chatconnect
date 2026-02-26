package com.chatConnect.backend.Event;

import java.util.List;

public class EmojiCreatedEvent {
    private long msgId;
    private String username1;
    private String username2;

    private List<String> emojis;

    public EmojiCreatedEvent(long msgId, String username1, String username2, List<String> emojis) {
        this.msgId = msgId;
        this.username1 = username1;
        this.username2 = username2;
        this.emojis = emojis;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public List<String> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<String> emojis) {
        this.emojis = emojis;
    }
}
