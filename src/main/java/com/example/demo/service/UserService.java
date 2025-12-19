package com.example.demo.service;

public interface UserService {

    String register(String name, String email);

    String login(String email);
}
