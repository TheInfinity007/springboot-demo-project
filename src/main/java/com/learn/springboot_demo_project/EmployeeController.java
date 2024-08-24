package com.learn.springboot_demo_project;

import org.aspectj.weaver.ast.Var;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee create(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    return new EmployeeNotFoundException(id);
                });
    }

    // Update employee
    @PutMapping("/employees/{id}")
    Employee updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Optional<Employee> employee = repository.findById(id);

        if (employee.isEmpty()) {
            System.out.println("Employee not found, creating a new employee");
            return repository.save(newEmployee);
        }

        System.out.println("Employee found with id " + id + " " + employee);

        Employee existingEmployee = employee.get();

        existingEmployee.setName(newEmployee.getName());
        existingEmployee.setRole(newEmployee.getRole());
        return repository.save(existingEmployee);
    }

    // Delete an employee by id
    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
