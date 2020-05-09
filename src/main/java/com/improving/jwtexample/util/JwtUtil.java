package com.improving.jwtexample.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//this will handle all JWT logic. Abstract it away
@Service
public class JwtUtil {
    private String SECRET_KEY = "secret";

    //take in token, use extractClaim method to examine the claim in given token, and return the username
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //take in token, use extractClaim method to examine the claim in given token, and return expiration date
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //takes in a token and resolves the claims (basically decodes the data in payload of given token)
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //User authenticates...come in here and create JWT using the UserDetails
    //will use this in outside classes
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        //can pass in whatever data you want to include in the payload as "claims"
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                //putting data into payload
                .setClaims(claims)
                //sets the user
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //current Date plus 10 hours...chose whatever length you want
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                //sign it with your preferred algorithm USING your secret key
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                //actually builds the JWT string that gets passed back to Client
                .compact();
    }

    //taken in user details and a token, checks if username in token matches username in user details AND token not expired
    //will use this in outside classes
    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
