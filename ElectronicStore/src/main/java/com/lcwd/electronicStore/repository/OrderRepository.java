package com.lcwd.electronicStore.repository;

import com.lcwd.electronicStore.entity.Order;
import com.lcwd.electronicStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);
}
