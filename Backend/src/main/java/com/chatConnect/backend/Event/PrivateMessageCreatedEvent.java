package com.chatConnect.backend.Event;

import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.ChatMessageResponseDTO;

public class PrivateMessageCreatedEvent {
    private ChatMessage chatMessage;
    private String status;

    public PrivateMessageCreatedEvent(ChatMessage chatMessage, String status) {
        this.chatMessage = chatMessage;
        this.status = status;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
