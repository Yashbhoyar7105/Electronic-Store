package com.lcwd.electronicStore.service.impl;

import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.dtos.ProductDto;
import com.lcwd.electronicStore.entity.Product;
import com.lcwd.electronicStore.exception.ResourceNotFoundException;
import com.lcwd.electronicStore.helper.Helper;
import com.lcwd.electronicStore.repository.ProductRepository;
import com.lcwd.electronicStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceimpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found with the given Id!! "));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());

        Product save = productRepository.save(product);

        return mapper.map(save, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found with the given Id!! "));
        productRepository.delete(product);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber,int pageSize,String SortBy,String SortDir) {
        Sort sort=(SortDir.equalsIgnoreCase("Desc"))?(Sort.by(SortBy).descending()):(Sort.by(SortBy).ascending());
        Pageable pageable= PageRequest.of( pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);

        return Helper.getPagebleResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto getSingle(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found with the given Id!! "));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String SortBy,String SortDir) {
        Sort sort=(SortDir.equalsIgnoreCase("Desc"))?(Sort.by(SortBy).descending()):(Sort.by(SortBy).ascending());
        Pageable pageable= PageRequest.of( pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);

        return Helper.getPagebleResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String SortBy,String SortDir) {
        Sort sort=(SortDir.equalsIgnoreCase("Desc"))?(Sort.by(SortBy).descending()):(Sort.by(SortBy).ascending());
        Pageable pageable= PageRequest.of( pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);

        return Helper.getPagebleResponse(page, ProductDto.class);
    }
}
