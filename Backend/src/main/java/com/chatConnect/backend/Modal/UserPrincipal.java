package com.chatConnect.backend.Modal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails,Principal {

    private final Users user;

    public UserPrincipal(Users user){
        this.user=user;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword(){
        System.out.println(user.getPassword());
        return user.getPassword();
    }

    @Override
    public boolean isEnabled(){
        return  true;
    }



    @Override
    public boolean isAccountNonLocked(){
        return  true;
    }

    @Override
    public String getUsername(){
        System.out.println(user.getUsername());
        return user.getUsername();
    }


    @Override
    public String getName() {
        return user.getUsername();
    }
}
