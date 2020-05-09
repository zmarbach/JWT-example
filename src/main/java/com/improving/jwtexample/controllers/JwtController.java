package com.improving.jwtexample.controllers;

import com.improving.jwtexample.domain.AuthenticationRequest;
import com.improving.jwtexample.domain.AuthenticationResponse;
import com.improving.jwtexample.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    private String hello() {
        return "Hello user! Congrats, you have been granted access.";
    }

    //user hits this endpoint to authenticate, request body contains username and password.
    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            //user Spring Security AuthManager to authenticate. If successful, then move on to generate jwtToken
            //UsernamePasswordAuthToken is the default token used by Spring Security
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        //get UserDetails obj from UserDetails service
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        //build the jwtToken
        final String jwtToken = jwtUtil.generateToken(userDetails);
        //return the jwtToken to client in the response
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}
