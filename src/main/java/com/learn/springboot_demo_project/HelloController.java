package com.learn.springboot_demo_project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    String index() {
        return "Greetings from Spring Boot";
    }
}
