package com.learn.springboot_demo_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerITest {

    @Autowired
    TestRestTemplate template;

//    Getting error with the TestRestTemplate passing in constructor
//    HelloControllerITest(TestRestTemplate template) {
//        this.template = template;
//    }

    @Test
    void getHello() {
        System.out.println("Running the test from HelloController I test");

        ResponseEntity<String> response = template.getForEntity("/", String.class);
        assertThat(response.getBody()).isEqualTo("Greetings from Spring Boot");

        System.out.println("Finished test from HelloController I test");
    }

}
