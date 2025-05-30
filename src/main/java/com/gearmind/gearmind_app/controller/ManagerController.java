package com.gearmind.gearmind_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {

    @GetMapping("/api/manager/test")
    public String managerTest() {
        return "Hello, Manager or Admin!";
    }
}