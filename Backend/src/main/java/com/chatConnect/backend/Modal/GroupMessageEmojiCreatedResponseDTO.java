package com.chatConnect.backend.Modal;

import java.util.Map;

public class GroupMessageEmojiCreatedResponseDTO {
    private Long msgId;
    private Map<String,Long> emojisMap;

    public GroupMessageEmojiCreatedResponseDTO(Long msgId, Map<String, Long> emojisMap) {
        this.msgId = msgId;
        this.emojisMap = emojisMap;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Map<String, Long> getEmojisMap() {
        return emojisMap;
    }

    public void setEmojisMap(Map<String, Long> emojisMap) {
        this.emojisMap = emojisMap;
    }
}
