package com.chatConnect.backend.Service;


import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageDetectionService {



    LanguageDetector detector;

    public LanguageDetectionService(){
        this.detector= LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.TAMIL, Language.HINDI).build();
    }

    public String detectLanguage(String text){
        //System.out.println("text="+text);

        Language language=detector.detectLanguageOf(text);
        //System.out.println(language);
        return language.getIsoCode639_1().toString();
    }
}
