package com.kruk.orderservice.repository;

import com.kruk.orderservice.domain.Order;
import com.kruk.orderservice.domain.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByDescription(String desc);

    @Query("select h from OrderStatusHistory h where h.order.id = :id")
    List<OrderStatusHistory> findOrderStatusHistoryById(long id);
}
