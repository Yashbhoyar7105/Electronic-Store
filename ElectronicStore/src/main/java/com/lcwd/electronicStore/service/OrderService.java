package com.lcwd.electronicStore.service;

import com.lcwd.electronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronicStore.dtos.OrderDto;
import com.lcwd.electronicStore.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //create Order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);

    //order get by user
    List<OrderDto> getOrderByUser(String userId);

    //get All Orders
    PageableResponse<OrderDto>  getAllOrders(int pageNumber,int pageSize, String sortBy, String sortDir);


}
