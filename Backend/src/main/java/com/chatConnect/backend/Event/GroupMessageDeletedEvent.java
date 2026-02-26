package com.chatConnect.backend.Event;

import com.chatConnect.backend.Modal.GroupMessage;

public class GroupMessageDeletedEvent {
    private GroupMessage groupMessage;

    public GroupMessageDeletedEvent(GroupMessage groupMessage) {
        this.groupMessage = groupMessage;
    }

    public GroupMessage getGroupMessage() {
        return groupMessage;
    }

    public void setGroupMessage(GroupMessage groupMessage) {
        this.groupMessage = groupMessage;
    }
}
