package com.lcwd.electronicStore.service.impl;

import com.lcwd.electronicStore.dtos.CategoryDto;
import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.entity.Category;
import com.lcwd.electronicStore.exception.ResourceNotFoundException;
import com.lcwd.electronicStore.helper.Helper;
import com.lcwd.electronicStore.repository.CategoryRepository;
import com.lcwd.electronicStore.service.CategoriesService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class CategoryServiceimpl implements CategoriesService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.profile.image.path}")
    private String imagepath;

    private Logger logger= LoggerFactory.getLogger(CategoryServiceimpl.class);

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        String catogeryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(catogeryId);

        Category category = modelMapper.map(categoryDto, Category.class);
        Category savecategory = categoryRepository.save(category);
        return modelMapper.map(savecategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category with given id is not found!!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category save = categoryRepository.save(category);

        return modelMapper.map(save, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category with given id is not found!!"));

        String fullpath = imagepath + category.getCoverImage();

        try {
            Path path = Paths.get(fullpath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("no such image is found in folder!!");
            ex.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }


        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize, sort );
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pagebleResponse = Helper.getPagebleResponse(page, CategoryDto.class);
        return pagebleResponse;
    }

    @Override
    public CategoryDto getSingle(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category with given id is not found!!"));
        return modelMapper.map(category, CategoryDto.class);
    }
}
