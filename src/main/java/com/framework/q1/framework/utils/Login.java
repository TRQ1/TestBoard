package com.framework.q1.framework.utils;


import com.framework.q1.framework.dao.UserDao;
import com.framework.q1.framework.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Login {

    SessionUtils sessionUtils = new SessionUtils();
    UserDao userDao = new UserDao();

    public void userLoignCheck(HttpServletRequest request, HttpServletResponse response, String userName, String userPasswd) throws IOException {

        String userGId = "";
        String userGPass = "";

        do {
            UserVo userVo = new UserVo();
            userDao.sqlLogin();
            userGId = userVo.getUserid();
            userGPass = userVo.getUserpasswd();
            userDao.getAuth(userName);

        } while (userName.equals(userGId) && userPasswd.equals(userGPass));
            sessionUtils.createSession(request, userName);
            sessionUtils.getSession(request, userName);
            response.sendRedirect("index.jsp");
    }

    public void vistorLoginCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        sessionUtils.createSession(request, "vistor");
        sessionUtils.getSession(request, "vistor");
        response.sendRedirect("index.jsp");
    }

    public String checkAuth(HttpServletRequest request, HttpServletResponse response, String userName) {
        int authCode = userDao.getAuth(userName);
        String userAuth = null;

        if(authCode == 1) {
            userAuth = "admin";
        } else if(authCode == 2) {
            userAuth = "gold";
        } else if(authCode == 3) {
            userAuth = "sliver";
        } else if (authCode == 4) {
            userAuth = "copper";
        } else if (authCode == 0){
            userAuth = "guest";
        }
        return userAuth;
    }

}