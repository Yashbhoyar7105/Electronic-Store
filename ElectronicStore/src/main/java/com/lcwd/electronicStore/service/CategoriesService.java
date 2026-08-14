package com.lcwd.electronicStore.service;

import com.lcwd.electronicStore.dtos.CategoryDto;
import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.entity.Category;

public interface CategoriesService {
    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get all
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single
    CategoryDto getSingle(String categoryId);
}
