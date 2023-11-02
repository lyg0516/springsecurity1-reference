package com.cos.springsecurity1.auth;

import com.cos.springsecurity1.model.User;
import com.cos.springsecurity1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 시큐리티에서 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailService 타입으로 되어있는 loadUserByName 함수가 실행

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    //시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user != null){
            return new PrincipalDetail(user);
        }
        return null;
    }
}
