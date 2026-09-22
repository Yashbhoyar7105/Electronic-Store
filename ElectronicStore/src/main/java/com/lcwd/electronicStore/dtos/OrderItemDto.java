package com.lcwd.electronicStore.dtos;

import com.lcwd.electronicStore.entity.Order;
import com.lcwd.electronicStore.entity.Product;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {


    private int orderItemId;
    private int quantity;
    private String totalPrice;
    private ProductDto product;
  
}
