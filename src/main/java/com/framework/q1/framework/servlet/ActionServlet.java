package com.framework.q1.framework.servlet;

import com.framework.q1.framework.action.Action;
import com.framework.q1.framework.action.DetailAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ActionServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String classpathStr = "action.DetailAction";

        Action action = new DetailAction();
        try {
            action.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        String classpathStr = "action.DetailAction";

        Action action = new DetailAction();
        try {
            action.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
