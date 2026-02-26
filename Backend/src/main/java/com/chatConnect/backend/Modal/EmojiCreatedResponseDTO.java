package com.chatConnect.backend.Modal;

import java.util.List;

public class EmojiCreatedResponseDTO {
    private long msgId;
    private List<String> emojis;

    public EmojiCreatedResponseDTO(long msgId, List<String> emojis) {
        this.msgId = msgId;
        this.emojis = emojis;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public List<String> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<String> emojis) {
        this.emojis = emojis;
    }
}
