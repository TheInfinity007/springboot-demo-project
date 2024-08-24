package com.learn.springboot_demo_project;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2")
public class EmployeeControllerV2 {

    private EmployeeRepository repository;
    private EmployeeModelAssembler assembler;

    EmployeeControllerV2(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<Employee> employees = repository.findAll();

        List<EntityModel<Employee>> mappedEmployee = employees.stream().map(employee ->
                        assembler.toModel(employee)
//                EntityModel.of(employee,
//                        linkTo(
//                                methodOn(EmployeeControllerV2.class)
//                                        .one(employee.getId())
//                        ).withSelfRel(),
//                        linkTo(
//                                methodOn(EmployeeControllerV2.class)
//                                        .all()
//                        ).withRel("employees")
//                )
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
    ResponseEntity<?> create(@RequestBody Employee newEmployee) {
        Employee employee = repository.save(newEmployee);

        EntityModel<Employee> data = assembler.toModel(employee);

        // ResponseEntity is used to create an HTTP 201 Created Response Status message.
        // created will a location response header
        return ResponseEntity.created(data.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(data);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    return new EmployeeNotFoundException(id);
                });

        return assembler.toModel(employee);

//        EntityModel.of(employee,
//                linkTo(
//                        methodOn(EmployeeControllerV2.class)
//                                .one(id)
//                ).withSelfRel(),
//                linkTo(
//                        methodOn(EmployeeControllerV2.class)
//                                .all()
//                ).withRel("employees")
//        );
    }

    // Update employee
    @PutMapping("/employees/{id}")
    ResponseEntity<?> updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Optional<Employee> employee = repository.findById(id);

        Employee updatedEmployee;
        if (employee.isEmpty()) {
            System.out.println("Employee not found, creating a new employee");
            updatedEmployee = repository.save(newEmployee);
        } else {
            System.out.println("Employee found with id " + id + " " + employee);

            Employee existingEmployee = employee.get();

            existingEmployee.setName(newEmployee.getName());
            existingEmployee.setRole(newEmployee.getRole());
            updatedEmployee = repository.save(existingEmployee);
        }

        EntityModel<Employee> data = assembler.toModel(updatedEmployee);

        return ResponseEntity.created(data.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(data);
    }

    // Delete an employee by id
    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }
}
