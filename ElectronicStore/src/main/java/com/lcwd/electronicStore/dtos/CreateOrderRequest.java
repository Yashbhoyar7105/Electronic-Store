package com.lcwd.electronicStore.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "userId is required!")
    private String userId;
    @NotBlank(message = "cartId is required!")
    private String cartId;

    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "Billingaddress is required!")
    private String billingAddress;
    @NotBlank(message = "billingphone is required!")
    private String billingPhone;
    @NotBlank(message = "billingName is required!")
    private String billingName;

}
