package com.lcwd.electronicStore.controller;


import com.lcwd.electronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronicStore.dtos.CartDto;
import com.lcwd.electronicStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    //create cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable String userId,
            @RequestBody AddItemToCartRequest request)
    {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }

    //remove item from cart

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,
                                                                 @PathVariable int itemId){

        cartService.removeItemFromCart(userId,itemId);

        ApiResponseMessage build = ApiResponseMessage.builder()
                .message("cart id removed!")
                .sucess(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(build,HttpStatus.OK);

    }

    //clear  cart
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){

        cartService.clearCart(userId);

        ApiResponseMessage build = ApiResponseMessage.builder()
                .message("now cart is clear!")
                .sucess(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(build,HttpStatus.OK);

    }

    //get cart by user
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(
            @PathVariable String userId)

    {
        CartDto cartByUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartByUser, HttpStatus.OK);

    }

}
