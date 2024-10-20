package com.pinup.global.oauth2;

import com.pinup.entity.Member;
import com.pinup.enums.LoginType;
import com.pinup.global.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.pinup.global.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.pinup.global.oauth2.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2UserInfo oAuth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo){
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(LoginType loginType,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes){

        if (loginType == LoginType.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }

        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
                                           Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(LoginType loginType, OAuth2UserInfo oAuth2UserInfo){
        return Member.builder()
                .loginType(loginType)
                .socialId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .nickname(oAuth2UserInfo.getNickname())
                .profileImage(oAuth2UserInfo.getImageUrl())
                .socialId(oAuth2UserInfo.getId())
                .build();
    }
}
