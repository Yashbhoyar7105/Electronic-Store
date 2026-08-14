package com.lcwd.electronicStore.repository;

import com.lcwd.electronicStore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {
}
