package com.lcwd.electronicStore.dtos;


import com.lcwd.electronicStore.entity.CartItem;
import com.lcwd.electronicStore.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {


    private String cartId;

    private UserDto user;
    private Date createdDate;

    private List<CartItemDto> items = new ArrayList<>();


}
