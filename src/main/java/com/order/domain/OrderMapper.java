package com.order.domain;

import com.order.domain.entity.Item;
import com.order.domain.entity.Order;
import com.order.domain.model.ItemDto;
import com.order.domain.model.OrderDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class OrderMapper {
    public static Order toOrder(OrderDto orderDto) {
        Order result = Order.builder()
                .id(orderDto.getId())
                .code(orderDto.getCode())
                .status(orderDto.getStatus())
                .valueTotal(orderDto.getValueTotal())
                .build();
        result.setItems(toItems(orderDto.getItems(), result));
        return result;
    }

    public static OrderDto toOrder(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .code(order.getCode())
                .status(order.getStatus())
                .valueTotal(order.getValueTotal())
                .items(toItemsDto(order.getItems()))
                .build();
    }

    private static List<ItemDto> toItemsDto(List<Item> items) {
        return Optional.ofNullable(items).orElse(Collections.emptyList())
                .stream().map(i -> ItemDto.builder()
                        .id(i.getId())
                        .name(i.getName())
                        .value(i.getValue())
                        .quantity(i.getQuantity())
                        .build()).toList();
    }

    private static List<Item> toItems(List<ItemDto> itemDtos, Order order) {
        return Optional.ofNullable(itemDtos).orElse(Collections.emptyList())
                .stream().map(dto -> Item.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .value(dto.getValue())
                        .quantity(dto.getQuantity())
                        .order(order)
                        .build())
                .toList();
    }

}
