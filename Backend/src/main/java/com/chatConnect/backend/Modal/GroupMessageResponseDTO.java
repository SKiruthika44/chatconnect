package com.chatConnect.backend.Modal;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GroupMessageResponseDTO {

    private long id;
    private String content;
    private LocalDateTime createdAt;

    private String status;

    private String senderName;

    private String groupName;

    private boolean deletedForEveryone=false;


    private Map<String,Long> emojisCount;

    public GroupMessageResponseDTO(long id,String content, LocalDateTime createdAt, String status, String senderName, String groupName) {
        this.id=id;
        this.content = content;
        this.createdAt = createdAt;
        this.status = status;
        this.senderName = senderName;
        this.groupName = groupName;
        emojisCount=new HashMap<>();
    }

    public String getContent() {
        return content;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isDeletedForEveryone() {
        return deletedForEveryone;
    }


    public Map<String, Long> getEmojisCount() {
        return emojisCount;
    }

    public void setEmojisCount(Map<String, Long> emojisCount) {
        this.emojisCount = emojisCount;
    }

    public void setDeletedForEveryone(boolean deletedForEveryone) {
        this.deletedForEveryone = deletedForEveryone;
    }
}
