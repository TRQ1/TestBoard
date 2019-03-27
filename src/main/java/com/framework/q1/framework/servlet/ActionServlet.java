package com.framework.q1.framework.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

public class ActionServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        execute(request, response);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Properties prop = new Properties();
            InputStream propPath = getClass().getClassLoader().getResourceAsStream("uriMapping.properties");
            prop.load(propPath);
            propPath.close();
            System.out.println("prop: " + prop);
            System.out.println("request.getRequestURI(): " + request.getRequestURI());

            String classpathStr = prop.getProperty(request.getRequestURI());
            System.out.println("classpathStr: " + classpathStr);

            Class<?> clz = Class.forName(classpathStr);
            Constructor constructor1 = clz.getConstructor(new Class[]{});
            Object classInstance = constructor1.newInstance(new Object[]{});
            Method executeMethod = clz.getMethod("execute", new Class[] { HttpServletRequest.class, HttpServletResponse.class });
            executeMethod.invoke(classInstance, new Object[] { request, response });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
