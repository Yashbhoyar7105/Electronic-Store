package com.lcwd.electronicStore.dtos;

import com.lcwd.electronicStore.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {


    private String productId;
    @NotBlank(message = "title is required!!")
    @Size(min = 4,message = "title required more than 4 char!!")
    private String title;
    @NotBlank(message = "description must be required!!")
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String imageName;

    private CategoryDto category;
}
