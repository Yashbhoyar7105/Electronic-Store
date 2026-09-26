package com.lcwd.electronicStore.controller;


import com.lcwd.electronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronicStore.dtos.ImageResponse;
import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.dtos.UserDto;
import com.lcwd.electronicStore.service.FileService;
import com.lcwd.electronicStore.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.OutputKeys;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    FileService fileService;

    @Value("${user.profile.image.path}")
    private String ImageUploadFile;

   private Logger logger= LoggerFactory.getLogger(UserController.class);

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid  @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return  new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateuser(@Valid @RequestBody UserDto userDto,
                                              @PathVariable String userId){
        UserDto userDto1 = userService.updateUser(userDto, userId);
        return  new ResponseEntity<>(userDto1, HttpStatus.OK);
    }
//    delete
@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("user deleted Sucessfully!!")
                .sucess(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
//    getall
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue="0", required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc", required = false) String sortDir
    ){

        return new ResponseEntity<>(userService.getAllUser( pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

//     getSingle
@GetMapping("/{userId}")
public ResponseEntity<UserDto> getByID(@PathVariable String userId){
    UserDto userById = userService.getUserById(userId);
    return new ResponseEntity<>(userById, HttpStatus.OK);

}
//       get by email
@GetMapping("/email/{email}")
public ResponseEntity<UserDto> getByEmail(@PathVariable String email){
    UserDto userByEmail = userService.getUserByEmail(email);
    return new ResponseEntity<>(userByEmail, HttpStatus.OK);

}

//    search usre
@GetMapping("/search/{keywords}")
public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){

    return new ResponseEntity<>(userService.searchUsers(keywords), HttpStatus.OK);

}

  @PostMapping("/images/{userId}")
  public  ResponseEntity<ImageResponse> uploadUserImage(
          @RequestParam("userImage")MultipartFile image,
          @PathVariable String userId) throws IOException {
      String imageName = fileService.uploadFile(image, ImageUploadFile);
      UserDto user = userService.getUserById(userId);
      user.setUserImage(imageName);
      UserDto userDto = userService.updateUser(user, userId);
      ImageResponse imageResponse= ImageResponse.builder().ImageName(imageName).message("image upload sucessfully!!").status(HttpStatus.CREATED).sucess(true).build();
      return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);


  }


  @GetMapping("/image/{userId}")
  public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
      UserDto user = userService.getUserById(userId);
      logger.info("user image : {} ",user.getUserImage());
      InputStream resource = fileService.getResource(ImageUploadFile, user.getUserImage());
      response.setContentType(MediaType.IMAGE_JPEG_VALUE);

      StreamUtils.copy(resource,response.getOutputStream());

  }

}
