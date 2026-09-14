package com.lcwd.electronicStore.repository;


import com.lcwd.electronicStore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
