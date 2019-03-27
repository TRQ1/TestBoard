package com.framework.q1.framework.utils;

import javax.servlet.http.HttpServletRequest;

public class SessionUtils {


    public void createSession(HttpServletRequest request, String userName) {
        String userVistor = null;
        String userAccount = null;

        if (userName != null && userName.equals("vistor")) {
            userVistor = "vistor";
            setSession(request, "sessionId", userVistor);
        } else if (userName != null && !userName.equals("vistor")){
            userAccount = userName;
            setSession(request, "sessionId", userAccount);
        }

    }


    public void setSession(HttpServletRequest request, String key, String value) {
       request.getSession().setAttribute(key, value);
    }

    public String getSession(HttpServletRequest request, String key) {
       String session = (String) request.getSession().getAttribute(key);
       return session;
    }

    public static void removeSession(HttpServletRequest request, String key) {
        request.getSession().removeAttribute(key);
    }
}
