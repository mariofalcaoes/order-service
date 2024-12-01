package com.order.controller;

import com.order.domain.model.OrderDto;
import com.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The OrderController class handles HTTP requests related to orders.
 * It provides endpoints to fetch all orders in a paginated form and create a new order.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PagedResourcesAssembler<OrderDto> pagedResourcesAssembler;

    /**
     * Fetches a paginated list of all orders.
     *
     * @param pageable the pagination information including page number, size, and sorting.
     * @return a paginated model containing a list of {@link OrderDto} wrapped in {@link EntityModel}.
     */
    @GetMapping(value = "/orders")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<OrderDto>> findAll(Pageable pageable) {
        log.info("Fetching all orders");
        return pagedResourcesAssembler.toModel(orderService.findAll(pageable));
    }

    /**
     * Creates a new order based on the provided order data.
     *
     * @param orderDto the data transfer object containing the order details to be created.
     * @return the created {@link OrderDto} object.
     */
    @PostMapping(value = "/order")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto create(@Valid @RequestBody OrderDto orderDto) {
        log.info("Creating order");
        return this.orderService.create(orderDto);
    }

}
