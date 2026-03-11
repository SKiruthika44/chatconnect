package com.chatConnect.backend.Service;

import com.chatConnect.backend.Api.TranslationApi;
import com.chatConnect.backend.Modal.Users;
import com.chatConnect.backend.Repo.UserRepo;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service

public class TranslationService {

    @Autowired

    UserRepo userRepo;

    @Autowired
    MessageService messageService;

    @Autowired
    TranslationApi translationApi;


    public ResponseEntity<String> translateText(String username, long msgId) {

        Users user=userRepo.findByUsername(username);
        String targetLang=user.getPreferredLanguage();
        String text= messageService.getMessageContentByMsgId(msgId);
        System.out.println("content"+text);
        String sourceLang=messageService.getTextLanguage(msgId);
        System.out.println("targetlanguage"+targetLang);
        String translatedText=translationApi.translate(text,sourceLang,targetLang);
        System.out.println("translated text"+translatedText);
        return ResponseEntity.status(HttpStatus.OK).body(translatedText);

    }
}
