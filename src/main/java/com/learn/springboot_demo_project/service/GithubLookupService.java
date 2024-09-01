package com.learn.springboot_demo_project.service;

import com.learn.springboot_demo_project.entity.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class GithubLookupService {
    private static final Logger logger = LoggerFactory.getLogger(GithubLookupService.class);

    private final RestTemplate restTemplate;

    GithubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<GithubUser> findUser(String user, long sleepTimeInMs) throws InterruptedException {
        System.out.println("Looking up " + user + " Thread: " + Thread.currentThread().getName());
        String url = String.format("https://api.github.com/users/%s", user);
        System.out.println("URL " + url + " Thread: " + Thread.currentThread().getName());
        GithubUser result = restTemplate.getForObject(url, GithubUser.class);

        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(sleepTimeInMs);

        return CompletableFuture.completedFuture(result);
    }

    public GithubUser findUserSync(String user, long sleepTimeInMs) throws InterruptedException {
        System.out.println("Looking up " + user + " Thread: " + Thread.currentThread().getName());
        String url = String.format("https://api.github.com/users/%s", user);
        System.out.println("URL " + url + " Thread: " + Thread.currentThread().getName());
        GithubUser result = restTemplate.getForObject(url, GithubUser.class);

        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(sleepTimeInMs);

        return result;
    }
}
