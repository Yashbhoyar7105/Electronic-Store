package com.lcwd.electronicStore.repository;

import com.lcwd.electronicStore.entity.Cart;
import com.lcwd.electronicStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUSer(User user);
}
