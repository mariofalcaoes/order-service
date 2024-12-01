package com.order.repository;

import com.order.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Page<Order> findAllByOrderByIdDesc(Pageable pageable);

    Optional<Order> findByCode(String code);

}
