package com.kiruthika.chatapp.messaging_service.service;


import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import org.springframework.stereotype.Service;

@Service
public class LanguageDetectionService {
    private final LanguageDetector detector;

    public LanguageDetectionService(){
        this.detector=LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.TAMIL, Language.HINDI,Language.MALAY,Language.BENGALI,Language.URDU).build();
    }

    public String findLanguage(String text){
        Language lang=detector.detectLanguageOf(text);
        return lang.getIsoCode639_1().toString();

    }

}
