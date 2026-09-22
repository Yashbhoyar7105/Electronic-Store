package com.lcwd.electronicStore.dtos;


import com.lcwd.electronicStore.entity.OrderItem;
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
@ToString
public class OrderDto {


    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;
    private List<OrderItemDto> orderItems=new ArrayList<>();
}
