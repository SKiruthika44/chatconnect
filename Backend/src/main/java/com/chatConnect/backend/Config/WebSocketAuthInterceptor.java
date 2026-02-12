package com.chatConnect.backend.Config;

import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Service.JWTService;
import com.chatConnect.backend.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component

public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private JWTService jwtService;
    @Autowired
    ApplicationContext context;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {


        StompHeaderAccessor accessor= MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Invalid Authorization header");
            }



            token = token.substring(7);

            String username = jwtService.extractUserName(token);

            UserDetails userDetails = context
                    .getBean(MyUserDetailsService.class)
                    .loadUserByUsername(username);

            UserPrincipal userPrincipal =  (UserPrincipal) userDetails;;



            if (!jwtService.validateToken(token, userDetails)) {
                throw new IllegalArgumentException("Token is invalid");
            }



            accessor.setUser(userPrincipal);

        }
        return message;



    }
}
