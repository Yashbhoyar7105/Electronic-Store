package com.lcwd.electronicStore.repository;

import com.lcwd.electronicStore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);
}
