package com.chatConnect.backend.Controller;

import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Config.PresenceEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired

    private PresenceEventListener presenceEventListener;

    @MessageMapping("/ready")


    public void handleReady(@AuthenticationPrincipal UserPrincipal userPrincipal){

        presenceEventListener.broadcastPrivate(userPrincipal);
        presenceEventListener.pushGroupInfoToUser(userPrincipal);
        presenceEventListener.deliverPrivateMessages(userPrincipal);
        presenceEventListener.deliverGroupMessages(userPrincipal);

    }
}
