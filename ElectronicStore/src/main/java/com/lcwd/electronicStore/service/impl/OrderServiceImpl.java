package com.lcwd.electronicStore.service.impl;

import com.lcwd.electronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronicStore.dtos.OrderDto;
import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.entity.*;
import com.lcwd.electronicStore.exception.BadApiRequestException;
import com.lcwd.electronicStore.exception.ResourceNotFoundException;
import com.lcwd.electronicStore.helper.Helper;
import com.lcwd.electronicStore.repository.CartRepository;
import com.lcwd.electronicStore.repository.OrderRepository;
import com.lcwd.electronicStore.repository.UserRepository;
import com.lcwd.electronicStore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId=orderDto.getUserId();
        String cartId= orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given id is not found!"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart  with given id is not found!"));

        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size()<=0){
            throw new BadApiRequestException("Invalid Number of items in cart!!");
        }


        Order order = Order.builder()
                .billingAddress(orderDto.getBillingAddress())
                .billingName(orderDto.getBillingName())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(orderDto.getOrderStatus())
                .paymentStatus(orderDto.getPaymentStatus())
                .billingPhone(orderDto.getBillingPhone())
                .orderedDate(new Date())
                .deliveredDate(null)
                .user(user)
                .build();

        AtomicReference<Double> orderAmount= new AtomicReference<>(0.0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .product(cartItem.getProduct())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savdeOrder = orderRepository.save(order);


        return modelMapper.map(savdeOrder,OrderDto.class );
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("given order is not found by given id!"));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrderByUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("usre id is not found!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());


        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);

        return Helper.getPagebleResponse(page, OrderDto.class);
    }
}
