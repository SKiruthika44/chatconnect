package com.chatConnect.backend.Controller;

import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Service.EmojiService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class EmojiController {
    @Autowired
    private EmojiService emojiService;

    @PutMapping("/message/emoji")

    public ResponseEntity<Void> reactEmoji(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam long msgId, @RequestParam String msgType, String emoji){
        System.out.println("request came");
        return emojiService.reactEmoji(userPrincipal.getUsername(),msgId,msgType,emoji);
    }

    @PutMapping("/message/edit/{msgId}")
    public ResponseEntity<Void> editMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long msgId, @RequestParam String type,@RequestParam String content){
        System.out.println("in edit controller");
        return emojiService.editMessage(userPrincipal.getUsername(),msgId,type,content);
    }
}
