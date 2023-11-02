package com.cos.springsecurity1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable() // 이거해야 POST 요청이 가능하다.
                .authorizeRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다.
                .defaultSuccessUrl("/");
        return http.build();
    }
}
