package com.lcwd.electronicStore.service;

import com.lcwd.electronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronicStore.dtos.CartDto;
import com.lcwd.electronicStore.entity.User;

public interface CartService {

    //to add item in cart
    // case 1: cart available : add item to cart
    //case 2: cart not available : create new cart and add item to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //clear cart
    void clearCart(String userId);


    CartDto getCartByUser(String userId);
}
