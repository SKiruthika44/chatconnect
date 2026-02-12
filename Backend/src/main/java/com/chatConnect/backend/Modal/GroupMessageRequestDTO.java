package com.chatConnect.backend.Modal;

public class GroupMessageRequestDTO {
    private String groupName;
    private String content;

    public GroupMessageRequestDTO(String groupName, String content) {
        this.groupName = groupName;
        this.content = content;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
