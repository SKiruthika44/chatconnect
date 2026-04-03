package com.kiruthika.chatapp.ws_service.Config;

import com.kiruthika.chatapp.ws_service.client.UserServiceClient;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class PresenceEventListener {
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }

    @EventListener
    public void handleConnectedEvent(SessionConnectEvent event) throws InterruptedException {
        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());

        Principal principal=accessor.getUser();
        if(principal!=null){
            onlineUsers.add(principal.getName());
            broadCastOnline();
            System.out.println("Onlineusers:"+onlineUsers);

        }

    }

    @EventListener
    public void handleDisConnectedEvent(SessionDisconnectEvent event) throws InterruptedException {
        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());

        Principal principal=accessor.getUser();
        if(principal!=null){
            onlineUsers.remove(principal.getName());
            broadCastOnline();
            broadCastLastseen(principal.getName());
            System.out.println("Disconnect="+onlineUsers);

        }




    }

    private void broadCastLastseen(String username) {
            userServiceClient.updateLastSeen(username, LocalDateTime.now());

    }

    private void broadCastOnline() {
        simpMessagingTemplate.convertAndSend("/topic/online",onlineUsers);
    }
}
