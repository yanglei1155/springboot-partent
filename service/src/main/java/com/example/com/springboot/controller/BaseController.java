package com.example.com.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseController<T> {
    @Autowired
    protected  T service;
}
