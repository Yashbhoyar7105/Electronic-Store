package com.lcwd.electronicStore.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Categories")
public class Category {

    @Id
    @Column(name = "Category_Id")
    private String categoryId;
    @Column(name = "Category_Title", length = 50, nullable = false)
    private String title;
    @Column(name = "Category_description", length = 60)
    private String description;
    private String coverImage;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products= new ArrayList<>();


}
