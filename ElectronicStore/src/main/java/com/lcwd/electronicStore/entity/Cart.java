package com.lcwd.electronicStore.entity;


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
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private String CartId;
    @OneToOne
    private User user;
    private Date createdDate;

    @OneToMany( mappedBy = "cart",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> items= new ArrayList<>();



}
