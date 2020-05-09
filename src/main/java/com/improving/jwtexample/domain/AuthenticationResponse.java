package com.improving.jwtexample.domain;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationResponse {
    private String JwtToken;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(java.lang.String jwtToken) {
        JwtToken = jwtToken;
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }
}
