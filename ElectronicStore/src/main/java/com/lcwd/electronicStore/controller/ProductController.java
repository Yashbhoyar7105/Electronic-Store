package com.lcwd.electronicStore.controller;

import com.lcwd.electronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronicStore.dtos.ImageResponse;
import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.dtos.ProductDto;
import com.lcwd.electronicStore.repository.ProductRepository;
import com.lcwd.electronicStore.service.FileService;
import com.lcwd.electronicStore.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    FileService fileService;

    @Value("${product.image.path}")
    private String imageUploadFile;


    //create
    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto){
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(
            @PathVariable String productId,
            @RequestBody ProductDto productDto
    ){
        ProductDto productDto1 = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId){
        productService.deleteProduct(productId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Product sucessfully deleted!!").sucess(true).status(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId){
        ProductDto single = productService.getSingle(productId);
        return new ResponseEntity<>(single, HttpStatus.OK);
    }
    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam (name = "pageSize", defaultValue = "4", required = false) int pageSize,
            @RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam (name = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);

    }
    //Search by Title
    @GetMapping("/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchByTitle(
            @PathVariable String subTitle,
            @RequestParam (name = "pageSize", defaultValue = "4", required = false) int pageSize,
            @RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam (name = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir){
        PageableResponse<ProductDto> response = productService.searchByTitle(subTitle, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAlllive(
            @RequestParam (name = "pageSize", defaultValue = "4", required = false) int pageSize,
            @RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam (name = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProducts = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);

    }

    @PostMapping("/images/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {
        String imageName = fileService.uploadFile(image, productId);
        ProductDto product = productService.getSingle(productId);
        product.setImageName(imageName);
        ProductDto productDto = productService.updateProduct(product, productId);
        ImageResponse imageResponse = ImageResponse.builder().ImageName(imageName).message("Product iamge is sucesfully uploaded!!").status(HttpStatus.CREATED).sucess(true).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/images/{productId}")
    public void uploadImage(
            @PathVariable String productId,
           HttpServletResponse response
    ) throws IOException {
        ProductDto product = productService.getSingle(productId);
        InputStream resource = fileService.getResource(imageUploadFile, product.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());
    }

}
