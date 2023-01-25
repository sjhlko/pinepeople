package com.lion.pinepeople.config.security.filter;

import java.util.Base64;

public class JwtHelper {

    public static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}
