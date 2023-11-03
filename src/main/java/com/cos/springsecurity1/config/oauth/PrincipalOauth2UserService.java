package com.cos.springsecurity1.config.oauth;

import com.cos.springsecurity1.auth.PrincipalDetail;
import com.cos.springsecurity1.config.oauth.provider.FacebookUserInfo;
import com.cos.springsecurity1.config.oauth.provider.GoogleUserInfo;
import com.cos.springsecurity1.config.oauth.provider.NaverUserInfo;
import com.cos.springsecurity1.config.oauth.provider.OAuth2UserInfo;
import com.cos.springsecurity1.model.User;
import com.cos.springsecurity1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("UserRequest:"+userRequest);
        //구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리)
        // -> AccessToken요청 -> userRequest정보 -> 회원프로필 받아야함
        // 이때사용되는 함수가 loadUser함수: 회원 프로필을 받을 수 있음
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(userRequest.getClientRegistration().getRegistrationId());
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }


        String provider = oAuth2UserInfo.getProvider();
        String providerID = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerID;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            userEntity = User.builder()
                            .username(username)
                            .password(password)
                            .email(email)
                            .provider(provider)
                            .providerId(providerID)
                            .role(role)
                            .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetail(userEntity, oAuth2User.getAttributes());
    }
}
