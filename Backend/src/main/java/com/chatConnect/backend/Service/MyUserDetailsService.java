package com.chatConnect.backend.Service;

import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Modal.Users;
import com.chatConnect.backend.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo urepo;

    public MyUserDetailsService(UserRepo repo){
        this.urepo=repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=urepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found");

        }

        return new UserPrincipal(user);
    }
}
