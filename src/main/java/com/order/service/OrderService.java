package com.order.service;

import com.order.domain.OrderMapper;
import com.order.domain.entity.Item;
import com.order.domain.entity.Order;
import com.order.domain.entity.Status;
import com.order.domain.model.OrderDto;
import com.order.exception.BusinessException;
import com.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for business logic related to orders.
 * Includes operations for finding, creating, and validating orders.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * Finds all orders, ordered by ID in descending order, with pagination support.
     *
     * @param pageable the pagination information
     * @return a page of OrderDto objects
     */
    @Cacheable("orders")
    public Page<OrderDto> findAll(Pageable pageable) {
        return this.orderRepository.findAllByOrderByIdDesc(pageable)
                .map(OrderMapper::toOrder);
    }

    /**
     * Creates a new order based on the provided OrderDto.
     * Validates the order, calculates the total value, and saves it to the repository.
     *
     * @param orderDto the order data to be created
     * @return the created OrderDto
     * @throws BusinessException if the order code already exists
     */
    public OrderDto create(OrderDto orderDto) {
        log.info("Creating order with data {}", orderDto);
        Order order = OrderMapper.toOrder(orderDto);
        validateOrder(order);  // Ensure the order code doesn't already exist
        order.setStatus(Status.AUTHORIZED);  // Set default status to AUTHORIZED
        order.setValueTotal(calculateTotalValue(order.getItems()));  // Calculate the total value of the order
        Order orderSaved = this.orderRepository.save(order);
        log.info("Order saved with success id {} for code {}", orderSaved.getId(), orderSaved.getCode());
        return OrderMapper.toOrder(orderSaved);
    }

    /**
     * Validates the given order by checking if an order with the same code already exists.
     *
     * @param order the order to be validated
     * @throws BusinessException if an order with the same code already exists
     */
    private void validateOrder(Order order) {
        Optional<Order> orderExisting = this.orderRepository.findByCode(order.getCode());
        if (orderExisting.isPresent()) {
            throw new BusinessException(String.format("Order with code %s already exists", order.getCode()),
                    HttpStatus.PRECONDITION_FAILED);
        }
    }

    /**
     * Calculates the total value of the order based on the individual item values.
     *
     * @param items the list of items in the order
     * @return the total value of the order
     */
    private BigDecimal calculateTotalValue(List<Item> items) {
        return items.stream()
                .map(Item::getValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
