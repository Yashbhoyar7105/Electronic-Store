package com.lcwd.electronicStore.controller;


import com.lcwd.electronicStore.dtos.*;
import com.lcwd.electronicStore.entity.Category;
import com.lcwd.electronicStore.service.CategoriesService;
import com.lcwd.electronicStore.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    FileService fileService;

    @Value("${category.profile.image.path}")
    private String ImageUploadFiles;

    private Logger logger= LoggerFactory.getLogger(CategoryController.class);

    //creat
    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoriesService.createCategory(categoryDto);
        return  new ResponseEntity<>(category, HttpStatus.OK);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(
           @PathVariable String categoryId,
           @Valid  @RequestBody CategoryDto categoryDto
    ){
        CategoryDto categoryDto1 = categoriesService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(categoryDto1,HttpStatus.OK);
    }

    //detele
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String categoryId){

        categoriesService.deleteCategory(categoryId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Category sucessfully deleted!!").sucess(true).status(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "PageNumber", defaultValue = "0", required = false) int PageNumber,
            @RequestParam(value = "PageSize", defaultValue = "4", required = false)int PageSize,
            @RequestParam(value = "SortBy", defaultValue = "title", required = false)String SortBY,
            @RequestParam(value = "SortDir", defaultValue = "asc", required = false)String SortDir
    ){
        PageableResponse<CategoryDto> all = categoriesService.getAll(PageNumber, PageSize, SortBY, SortDir);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    //get
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> get(@PathVariable String categoryId){
        CategoryDto single = categoriesService.getSingle(categoryId);
        return new ResponseEntity<>(single,HttpStatus.OK);
    }
    @PostMapping("/image/{categoryId}")
    public  ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("coverImage") MultipartFile image,
            @PathVariable String categoryId) throws IOException {
        String imageName = fileService.uploadFile(image, ImageUploadFiles);
        CategoryDto category = categoriesService.getSingle(categoryId);
        category.setCoverImage(imageName);
        CategoryDto categoryDto = categoriesService.updateCategory(category, categoryId);
        ImageResponse imageResponse= ImageResponse.builder().ImageName(imageName).message("image upload sucessfully!!").status(HttpStatus.CREATED).sucess(true).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);


    }

    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category =categoriesService.getSingle(categoryId);
        logger.info("user image : {} ",category.getCoverImage());
        InputStream resource = fileService.getResource(ImageUploadFiles, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());

    }
}
