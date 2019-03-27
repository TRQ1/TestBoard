package com.framework.q1.framework.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

public class CookieUtils {

    public void createCookie(HttpServletResponse response, String cookieName, String userId) {
        Cookie cookie = new Cookie(cookieName, userId);
        cookie.setMaxAge(365 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String checkLogin(HttpServletRequest request, String userId) {
        String cookieUser = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(userId)) {
                    cookieUser = cookie.getValue();
                }
            }
        }
        return cookieUser;
    }


    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String userId) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(userId)) {
                    cookies[i].setMaxAge(0);
                    response.addCookie(cookies[i]);
                }
            }
        }
    }

    public String generateRandomString(int size) {
        String chars[] = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,1,2,3,4,5,6,7,8,9".split(",");
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }


    public void checkId(HttpServletRequest request, String loginId) {

        if (loginId == null || loginId.equals("null")) {
            loginId = "null";
        }

    }
}
