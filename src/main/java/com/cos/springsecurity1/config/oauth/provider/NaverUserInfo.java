package com.cos.springsecurity1.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attritues;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attritues = attributes;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String)attritues.get("id");
    }

    @Override
    public String getEmail() {
        return (String)attritues.get("email");
    }

    @Override
    public String getName() {
        return (String)attritues.get("name");
    }
}
