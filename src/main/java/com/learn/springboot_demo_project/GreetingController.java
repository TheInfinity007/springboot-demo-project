package com.learn.springboot_demo_project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    AtomicLong counter = new AtomicLong();
    String template = "Hello, %s!";

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue = "World") String name){
        String greetMessage = String.format(this.template, name);
        Greeting response = new Greeting(counter.incrementAndGet(), greetMessage);

        return response;
    }
}

