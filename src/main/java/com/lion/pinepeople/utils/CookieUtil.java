package com.lion.pinepeople.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void saveCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        response.addCookie(cookie);
    }

    public static void savePathCookie(HttpServletResponse response, String key, String value, String path) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void initCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] list = request.getCookies();
        if (list == null) return null;
        String value = null;
        for (Cookie cookie : list) {
            if (cookie.getName().equals(key)) {
                value = cookie.getValue();
                return value;
            }
        }
        return null;
    }
}
