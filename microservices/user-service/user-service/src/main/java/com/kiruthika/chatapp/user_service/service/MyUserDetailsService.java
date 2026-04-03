package com.kiruthika.chatapp.user_service.service;

import com.kiruthika.chatapp.user_service.modal.User;
import com.kiruthika.chatapp.user_service.modal.UserPrincipal;
import com.kiruthika.chatapp.user_service.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found");

        }

        return new UserPrincipal(user);
    }
}
