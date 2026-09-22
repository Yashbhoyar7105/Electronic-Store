package com.lcwd.electronicStore.repository;

import com.lcwd.electronicStore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
