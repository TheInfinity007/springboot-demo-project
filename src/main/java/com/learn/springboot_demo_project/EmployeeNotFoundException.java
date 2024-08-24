package com.learn.springboot_demo_project;

public class EmployeeNotFoundException extends RuntimeException {
    EmployeeNotFoundException(Long id) {
        super("Employee not found with id " + id);
    }
}
