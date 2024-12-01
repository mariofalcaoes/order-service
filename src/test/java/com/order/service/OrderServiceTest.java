package com.order.service;

import com.order.domain.entity.Item;
import com.order.domain.entity.Order;
import com.order.domain.entity.Status;
import com.order.domain.model.ItemDto;
import com.order.domain.model.OrderDto;
import com.order.exception.BusinessException;
import com.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void findAll() {
        Pageable pageable = Page.empty().getPageable();
        when(orderRepository.findAllByOrderByIdDesc(any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(new Order()),
                        pageable, 1));
        Page<OrderDto> allRecords = orderService.findAll(pageable);
        assertNotNull(allRecords);
        assertEquals(1, allRecords.getTotalPages());
        assertEquals(1, allRecords.getTotalElements());
    }

    @Test
    void create() {
        OrderDto orderDto = OrderDto.builder()
                .code("123321")
                .items(Collections.singletonList(ItemDto.builder()
                        .quantity(2)
                        .value(BigDecimal.ONE)
                        .name("coca-cola")
                        .build()))
                .build();
        Order order = Order.builder()
                .code(orderDto.getCode())
                .status(Status.AUTHORIZED)
                .valueTotal(BigDecimal.ONE)
                .items(Collections.singletonList(Item.builder()
                        .quantity(2)
                        .value(BigDecimal.ONE)
                        .name("coca-cola")
                        .build()))
                .build();
        Mockito.doReturn(order).when(this.orderRepository).save(any(Order.class));
        OrderDto result = this.orderService.create(orderDto);
        assertNotNull(result);
        assertEquals(Status.AUTHORIZED, result.getStatus());
        assertEquals(BigDecimal.ONE, result.getValueTotal());
    }

    @Test
    void createWithDuplication() {
        OrderDto orderDto = OrderDto.builder()
                .code("123321")
                .items(Collections.singletonList(ItemDto.builder()
                        .quantity(2)
                        .value(BigDecimal.ONE)
                        .name("coca-cola")
                        .build()))
                .build();
        Mockito.doReturn(Optional.of(new Order())).when(orderRepository).findByCode(anyString());
        assertThrows(BusinessException.class, () -> this.orderService.create(orderDto));
    }
}