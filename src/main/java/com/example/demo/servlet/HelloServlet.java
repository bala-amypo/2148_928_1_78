package com.example.demo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Hello from HelloServlet");
    }
    
    // Add a public wrapper method for testing
    public void processGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        doGet(request, response);
    }
}