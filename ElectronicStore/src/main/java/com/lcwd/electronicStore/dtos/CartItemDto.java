package com.lcwd.electronicStore.dtos;

import com.lcwd.electronicStore.entity.Cart;
import com.lcwd.electronicStore.entity.Product;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {


    private String cartItemId;

    private ProductDto product;
    private int quantity;
    private double totalPrice;

}
