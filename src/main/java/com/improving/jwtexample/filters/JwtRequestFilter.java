package com.improving.jwtexample.filters;

import com.improving.jwtexample.services.MyUserDetailsService;
import com.improving.jwtexample.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    //send
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get the item in the header labeled as "Authorization"...this will have the JWT
        final String authHeader = request.getHeader("Authorization");
        String userName = null;
        String jwtToken = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            //get just the JWT...comes after "Bearer"
            jwtToken = authHeader.split(" ")[1];
            //get the userName from JWT
            userName = jwtUtil.extractUsername(jwtToken);
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //get userDetails from service using userName
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            //if its a valid token then do the following
            if(jwtUtil.validateToken(jwtToken, userDetails)){
                //SPRING SECURITY WOULD NORMALLY DO THESE STEPS BELOW AUTOMATICALLY...BUT WE HAVE TO SPECIFY IT NOW SINCE WE "TOOK OVER". ONLY DO THIS IF TOKEN IS VALID!!!!

                //Build Spring Security DEFAULT token using the userDetails
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //set details using data in the request
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //authenticate the token in the Security Context
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //let Spring continue the filter chain
        filterChain.doFilter(request, response);
    }
}
