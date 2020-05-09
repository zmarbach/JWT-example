package com.improving.jwtexample.config;

import com.improving.jwtexample.filters.JwtRequestFilter;
import com.improving.jwtexample.services.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//This annotation signals to Spring Security that it needs to call the configure method below
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    MyUserDetailsService myUserDetailsService;
    JwtRequestFilter jwtRequestFilter;

    public SecurityConfigurer(MyUserDetailsService myUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    //this method tells Spring Security to allow client to hit the /authenticate endpoint w/o being authenticated first
    //but for any other endpoints...require them to be authenticated
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests().antMatchers("/authenticate").permitAll()
            .anyRequest().authenticated()
            //Tells Spring NOT to create and manage Sessions, cuz whole reason we do JWT is to make app stateless. This app doesn't have to remember anything.
            //JWT in the request header from client will tell app everything it needs to know (no need to maintain session list)
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //Tells Spring that for each request, use our custom JWT filter BEFORE default userNamePassWordAuthFilter is called
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //Tells Spring Security to treat incoming password (since we hardcoded) as is and do NOT do any encoding
    //Have to define a bean in order to get app running
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    //might need to use this if Spring doesn't not recognize AuthManager bean
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
