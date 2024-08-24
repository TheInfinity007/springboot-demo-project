package com.learn.springboot_demo_project;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2")
public class EmployeeControllerV2 {

    private EmployeeRepository repository;

    EmployeeControllerV2(EmployeeRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<Employee> employees = repository.findAll();

        List<EntityModel<Employee>> mappedEmployee = employees.stream().map(employee ->
                EntityModel.of(employee,
                        linkTo(
                                methodOn(EmployeeControllerV2.class)
                                        .one(employee.getId())
                        ).withSelfRel(),
                        linkTo(
                                methodOn(EmployeeControllerV2.class)
                                        .all()
                        ).withRel("employees")
                )
        ).collect(Collectors.toList());

        return CollectionModel.of(
                mappedEmployee,
                linkTo(
                        methodOn(EmployeeControllerV2.class)
                                .all()
                ).withSelfRel()
        );
    }

    @PostMapping("/employees")
    Employee create(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    return new EmployeeNotFoundException(id);
                });

        return EntityModel.of(employee,
                linkTo(
                        methodOn(EmployeeControllerV2.class)
                                .one(id)
                ).withSelfRel(),
                linkTo(
                        methodOn(EmployeeControllerV2.class)
                                .all()
                ).withRel("employees")
        );
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
