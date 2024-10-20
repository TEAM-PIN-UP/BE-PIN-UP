package com.pinup.global.oauth2.service;

import com.pinup.entity.Member;
import com.pinup.enums.LoginType;
import com.pinup.global.oauth2.CustomOAuth2User;
import com.pinup.global.oauth2.OAuthAttributes;
import com.pinup.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Autowired
    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        LoginType loginType = getLoginType(registrationId);
        OAuthAttributes extractAttributes = OAuthAttributes.of(loginType, userNameAttributeName, attributes);

        Member createdMember = getOrSaveMember(extractAttributes, loginType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getRole().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getEmail(),
                createdMember.getRole());
    }

    private Member getOrSaveMember(OAuthAttributes attributes, LoginType loginType) {
        Member findMember = memberRepository
                        .findByLoginTypeAndSocialId(loginType, attributes.getOAuth2UserInfo().getId())
                        .orElse(null);

        if (findMember == null) {
            return saveMember(attributes, loginType);
        }

        return findMember;
    }

    private Member saveMember(OAuthAttributes attributes, LoginType loginType){

        Member createdMember = attributes.toEntity(loginType, attributes.getOAuth2UserInfo());

        return memberRepository.save(createdMember);
    }



    private LoginType getLoginType(String registrationId) {
        if (registrationId.equals("KAKAO")) {
            return LoginType.KAKAO;
        }
        return LoginType.GOOGLE;
    }
}
