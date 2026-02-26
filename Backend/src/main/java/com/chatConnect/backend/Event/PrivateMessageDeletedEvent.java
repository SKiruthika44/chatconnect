package com.chatConnect.backend.Event;

import com.chatConnect.backend.Modal.ChatMessage;

public class PrivateMessageDeletedEvent {
    private ChatMessage chatMessage;

    public PrivateMessageDeletedEvent(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
