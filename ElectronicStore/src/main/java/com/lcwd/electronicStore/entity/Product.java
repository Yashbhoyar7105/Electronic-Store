package com.lcwd.electronicStore.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    private String productId;
    private String title;
    @Column(length = 10000)
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private boolean live;
    private boolean stock;

}
