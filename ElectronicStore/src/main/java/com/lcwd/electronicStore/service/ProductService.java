package com.lcwd.electronicStore.service;

import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.dtos.ProductDto;

public interface ProductService {
    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto,String productId);

    //delete  --use always void
    void deleteProduct(String productId);

    //getAll
    PageableResponse<ProductDto> getAllProducts(int pageNumber,int pageSize,String SortBy,String SortDir);

    //get single
    ProductDto getSingle(String productId);

    //getAllLive
    PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String SortBy,String SortDir);

    //getsubTitleContaining
    PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String SortBy,String SortDip);
}
