package com.student.tkpmnc.finalproject.service.authentication;

import com.student.tkpmnc.finalproject.entity.RawUser;
import com.student.tkpmnc.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RawUser user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        var type = new ArrayList<GrantedAuthority>();
        type.add(new SimpleGrantedAuthority(user.getUserType().toString()));
        return new User(user.getUsername(), user.getPassword(),
                type);
    }
}
