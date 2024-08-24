package com.learn.springboot_demo_project;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    OrderRepository repository;

    OrderModelAssembler assembler;

    OrderController(OrderRepository repository, OrderModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Get All Orders
    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> all() {
        List<Order> orders = repository.findAll();

        List<EntityModel<Order>> data = orders.stream().map(order -> assembler.toModel(order))
                .toList();

        return CollectionModel.of(data,
                linkTo(
                        methodOn(OrderController.class)
                                .all()
                ).withSelfRel()
        );

    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> one(@PathVariable Long id) {

        Optional<Order> order = repository.findById(id);
        if (order.isEmpty()) {
            throw new OrderNotFoundException(id);
        }


        return assembler.toModel(order.get());
    }

    @PostMapping("/orders")
    ResponseEntity<?> newOrder(@RequestBody Order order) {

        order.setStatus(Status.IN_PROGRESS);
        Order newOrder = repository.save(order);

        EntityModel<Order> data = assembler.toModel(newOrder);

        return ResponseEntity.created(data.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(data);
    }

    @DeleteMapping("/orders/{id}/cancel")
    ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            order = repository.save(order);

            EntityModel<Order> orderModel = assembler.toModel(order);
            return ResponseEntity.ok().body(orderModel);
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(
                        Problem.create()
                                .withTitle("Method Not Allowed")
                                .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status")
                );
    }

    @PutMapping("/orders/{id}/complete")
    ResponseEntity<?> complete(@PathVariable Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            order = repository.save(order);

            EntityModel<Order> orderEntity = assembler.toModel(order);
            return ResponseEntity.ok().body(orderEntity);
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(
                        Problem.create()
                                .withTitle("Method Not Allowed")
                                .withDetail("You can't complete an order that is in the " + order.getStatus() + " status")
                );
    }
}
