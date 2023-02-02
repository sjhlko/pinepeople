package com.lion.pinepeople.domain.dto.user.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OAuthDto {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;

    public static OAuthDto of(String provider, String attributeKey, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, attributes);
            default:
                throw new RuntimeException("해당하는 소셜로그인 존재하지 않습니다.");
        }
    }

    public static OAuthDto ofGoogle(String attributeKey, Map<String, Object> attributes) {
        return OAuthDto.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }
}
