package com.learn.springboot_demo_project.controller;

import com.learn.springboot_demo_project.entity.GithubUser;
import com.learn.springboot_demo_project.service.GithubLookupService;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class GithubUserController {

    GithubLookupService githubLookupService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GithubUserController.class);

    GithubUserController(GithubLookupService githubLookupService) {
        this.githubLookupService = githubLookupService;
    }

    @GetMapping("/v1/github/user/{userId}")
    Object getUser(@PathVariable String userId) throws InterruptedException, ExecutionException {

        System.out.println("Input user with userId " + userId + " Thread: " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        CompletableFuture<GithubUser> userFuture = this.githubLookupService.findUser(userId, 5000);
        CompletableFuture<GithubUser> userFuture2 = this.githubLookupService.findUser("CloudFoundry", 5000);


        System.out.println("Elapsed time1: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
//        CompletableFuture.allOf(userFuture).join();
        userFuture.thenAccept(user -> {
            System.out.println("Elapsed time4: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
            System.out.println("User details " + user + " Thread: " + Thread.currentThread().getName());
        }).exceptionally(ex -> {
            System.out.println("An error occurred " + ex.getMessage() + " Thread: " + Thread.currentThread().getName());
            return null;
        });
        System.out.println("Elapsed time2: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
        userFuture2.thenAccept(user -> {
            System.out.println("Elapsed time5: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
            System.out.println("User details " + user + " Thread: " + Thread.currentThread().getName());
        }).exceptionally(ex -> {
            System.out.println("An error occurred " + ex.getMessage() + " Thread: " + Thread.currentThread().getName());
            return null;
        });

        System.out.println("userFuture before join " + userFuture + " Thread: " + Thread.currentThread().getName());
        System.out.println("Elapsed time3: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());

//        userFuture.join();// Wait for the CompletableFuture to complete (for demonstration purposes)

        // Similar to Promise.all(promise1, promise2);
        CompletableFuture.allOf(userFuture, userFuture2).join(); // block the current thread until the Completable future conpletes

        System.out.println("Elapsed time6: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
        System.out.println("userFuture after join " + userFuture + " Thread: " + Thread.currentThread().getName());

        System.out.println("Got the user back from the githubLookupService" + " Thread: " + Thread.currentThread().getName());
        System.out.println("Elapsed time7: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());

        return userFuture.get();
    }

    @GetMapping("/v1/github/user/{userId}/sync")
    GithubUser getUserSync(@PathVariable String userId) throws InterruptedException, ExecutionException {

        System.out.println("Input user with userId " + userId + " Thread: " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        System.out.println("Elapsed time1: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
        GithubUser user1 = this.githubLookupService.findUserSync(userId, 5000);
        System.out.println("Elapsed time2: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
        System.out.println("User details " + user1 + " Thread: " + Thread.currentThread().getName());
        GithubUser user2 = this.githubLookupService.findUserSync("CloudFoundry", 5000);
        System.out.println("Elapsed time3: " + (System.currentTimeMillis() - start) + " Thread: " + Thread.currentThread().getName());
        System.out.println("User details " + user2 + " Thread: " + Thread.currentThread().getName());

        return user1;
    }

}
