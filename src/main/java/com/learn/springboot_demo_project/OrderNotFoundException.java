package com.learn.springboot_demo_project;

public class OrderNotFoundException extends RuntimeException {
    OrderNotFoundException(Long id) {
        super("Order not found with id " + id);
    }
}
