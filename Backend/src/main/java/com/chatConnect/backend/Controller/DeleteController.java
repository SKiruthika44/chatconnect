package com.chatConnect.backend.Controller;


import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteController {
    @Autowired

    DeleteService deleteService;

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String type, @RequestParam long msgId,@RequestParam String scope){
        return deleteService.deleteMessage(userPrincipal.getUsername(),msgId,type,scope);
    }
}
