package com.improving.jwtexample.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    //would normally go to DB or something to load a user...here we just hardcode for example purposes
    //find the User obj, given a userName
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        return new User("foo", "foo", grantedAuthorities);
    }
}
