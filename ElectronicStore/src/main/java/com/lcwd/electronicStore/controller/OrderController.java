package com.lcwd.electronicStore.controller;


import com.lcwd.electronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronicStore.dtos.OrderDto;
import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .message("order is successsfully removed!")
                .sucess(true)
                .build();
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderByUser(@PathVariable String userId){

        List<OrderDto> orderByUser = orderService.getOrderByUser(userId);
        return new ResponseEntity<>(orderByUser,HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam (name = "pageSize", defaultValue = "4", required = false) int pageSize,
            @RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam (name = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
            @RequestParam (name = "sortDir", defaultValue = "desc", required = false) String sortDir
    ){

        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allOrders,HttpStatus.OK);

    }
}
