package com.chatConnect.backend.Event;

import com.chatConnect.backend.Modal.ChatMessage;

public class PrivateMessageEditedEvent {
    private ChatMessage chatMessage;

    public PrivateMessageEditedEvent(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
