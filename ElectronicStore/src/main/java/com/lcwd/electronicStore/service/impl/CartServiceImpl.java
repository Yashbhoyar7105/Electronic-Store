package com.lcwd.electronicStore.service.impl;


import com.lcwd.electronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronicStore.dtos.CartDto;
import com.lcwd.electronicStore.dtos.ProductDto;
import com.lcwd.electronicStore.entity.Cart;
import com.lcwd.electronicStore.entity.CartItem;
import com.lcwd.electronicStore.entity.Product;
import com.lcwd.electronicStore.entity.User;
import com.lcwd.electronicStore.exception.BadApiRequestException;
import com.lcwd.electronicStore.exception.ResourceNotFoundException;
import com.lcwd.electronicStore.repository.CartItemRepository;
import com.lcwd.electronicStore.repository.CartRepository;
import com.lcwd.electronicStore.repository.ProductRepository;
import com.lcwd.electronicStore.repository.UserRepository;
import com.lcwd.electronicStore.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private CartItemRepository cartItemRepository;


    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if(quantity<=0){
            throw new BadApiRequestException("requseted quantity is not valid!");
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found!!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("USer not found!!"));

        Cart cart=null;
        try{
            cart = cartRepository.findByUser(user).get();

        }catch (NoSuchElementException e){
           cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }


        boolean updated = false;

        List<CartItem> items = cart.getItems();

        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated = true;
                break;
            }
        }

        if (!updated) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("cart is not found with given id!!"));
       cartItemRepository.delete(cartItem1);

    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with the given id!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found with the given id!!"));
        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with the given id!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found with the given id!!"));

        return mapper.map(cart, CartDto.class);
    }
}
