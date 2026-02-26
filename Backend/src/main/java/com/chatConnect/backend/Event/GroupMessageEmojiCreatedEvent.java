package com.chatConnect.backend.Event;


import java.util.Map;

public class GroupMessageEmojiCreatedEvent {
    private Long messageId;
    private Map<String,Long> emojisMap;

    private Long groupId;

    public GroupMessageEmojiCreatedEvent(Long messageId, Map<String, Long> emojisMap,Long groupId) {
        this.messageId = messageId;
        this.emojisMap = emojisMap;
        this.groupId=groupId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Map<String,Long> getEmojisMap() {
        return emojisMap;
    }

    public void setEmojisMap(Map<String,Long> emojisMap) {
        this.emojisMap = emojisMap;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
