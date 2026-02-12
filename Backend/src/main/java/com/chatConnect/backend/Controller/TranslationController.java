package com.chatConnect.backend.Controller;

import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Service.ChatService;
import com.chatConnect.backend.Service.GroupService;
import com.chatConnect.backend.Service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class TranslationController {

    @Autowired
    TranslationService translationService;


    @GetMapping("/translate")

    public ResponseEntity<String> getTranslatedText(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam long msgId, @RequestParam String type){
        return translationService.translateText(userPrincipal.getUsername(),msgId,type);
    }


}
