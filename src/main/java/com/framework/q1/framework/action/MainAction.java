package com.framework.q1.framework.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainAction implements Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception  {
        String mapping = null;
        System.out.println("request: " + request);
        System.out.println("response: " + response);
    }
}
