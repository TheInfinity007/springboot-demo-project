package com.learn.springboot_demo_project;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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
}
