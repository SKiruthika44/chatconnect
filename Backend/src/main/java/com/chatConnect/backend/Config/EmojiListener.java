package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.EmojiCreatedEvent;
import com.chatConnect.backend.Event.GroupMessageEmojiCreatedEvent;
import com.chatConnect.backend.Modal.EmojiCreatedResponseDTO;
import com.chatConnect.backend.Modal.GroupMessageEmojiCreatedResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component

public class EmojiListener {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @EventListener
    public void notifyEmojiCreated(EmojiCreatedEvent emojiCreatedevent){
        EmojiCreatedResponseDTO emojiCreatedResponseDTO=new EmojiCreatedResponseDTO(emojiCreatedevent.getMsgId(),emojiCreatedevent.getEmojis());
        simpMessagingTemplate.convertAndSendToUser(emojiCreatedevent.getUsername1(),"/queue/private/emoji",emojiCreatedResponseDTO);
        simpMessagingTemplate.convertAndSendToUser(emojiCreatedevent.getUsername2(),"/queue/private/emoji",emojiCreatedResponseDTO);

    }

    @EventListener
    public void notifyGroupEmojiCreated(GroupMessageEmojiCreatedEvent groupMessageEmojiCreatedEvent){
        GroupMessageEmojiCreatedResponseDTO groupMessageEmojiCreatedResponseDTO=new GroupMessageEmojiCreatedResponseDTO(groupMessageEmojiCreatedEvent.getMessageId(), groupMessageEmojiCreatedEvent.getEmojisMap());


        Long groupId=groupMessageEmojiCreatedEvent.getGroupId();
        simpMessagingTemplate.convertAndSend("/topic/group/emoji/"+groupId,groupMessageEmojiCreatedResponseDTO);
        System.out.println("event sent to:"+groupMessageEmojiCreatedEvent.getGroupId());
    }
}
