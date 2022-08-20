package com.internal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;


@Component("myUsernamePasswordAuthenticationFilter")
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public MyUsernamePasswordAuthenticationFilter(
            AuthenticationFailureHandler authenticationFailureHandler
    ) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

}

