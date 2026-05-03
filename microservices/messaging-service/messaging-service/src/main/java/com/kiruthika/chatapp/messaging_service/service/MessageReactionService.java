package com.kiruthika.chatapp.messaging_service.service;


import com.kiruthika.chatapp.messaging_service.modal.DirectMessage;
import com.kiruthika.chatapp.messaging_service.modal.MessageReaction;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import com.kiruthika.chatapp.messaging_service.repo.MessageReactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MessageReactionService {

    private final MessageReactionRepo messageReactionRepo;
    public void reactMessage(Long msgId, Long userId, String emoji) {
        Boolean reacted=isMessageReacted(msgId,userId);
        if(reacted){
            MessageReaction messageReaction=messageReactionRepo.findByMsgIdAndUserId(msgId,userId);
            messageReaction.setEmoji(emoji);
            messageReactionRepo.save(messageReaction);

        }
        else{
            MessageUserId messageUserId=new MessageUserId(msgId, userId);

            MessageReaction reaction=new MessageReaction();
            reaction.setReactedAt(LocalDateTime.now());
            reaction.setId(messageUserId);
            reaction.setEmoji(emoji);
            messageReactionRepo.save(reaction);
        }
    }

    private Boolean isMessageReacted(Long msgId, Long userId) {
        return messageReactionRepo.isReacted(msgId,userId);
    }

    public List<String> getEmojis(long msgId) {
        List<String> emojis=messageReactionRepo.findEmojis(msgId);
        return emojis;
    }

    public Map<String, Long> getGroupMessageEmojis(long msgId) {
        List<Object[]> emojis=messageReactionRepo.findEmojisCountForMessage(msgId);
        Map<String, Long> emojisCount=new HashMap<>();
        for(Object[] row:emojis){
            emojisCount.put((String)row[0],(Long)row[1]);
        }
        return emojisCount;
    }
}
