package com.lcwd.electronicStore.service.impl;

import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.dtos.UserDto;
import com.lcwd.electronicStore.entity.User;
import com.lcwd.electronicStore.exception.ResourceNotFoundException;
import com.lcwd.electronicStore.helper.Helper;
import com.lcwd.electronicStore.repository.UserRepository;
import com.lcwd.electronicStore.service.UserService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagepath;

    private Logger logger= LoggerFactory.getLogger(UserServiceimpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {

        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user= dtoToentity(userDto);
        User savedUser = userRepository.save(user);
        UserDto newdto= entityTodto(savedUser);


        return newdto;
    }




    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found by given id!!"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setUserImage(userDto.getUserImage());

        User save = userRepository.save(user);
        UserDto userDto1 = entityTodto(save);
        return userDto1;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found by given id!!"));

        String fullpath = imagepath + user.getUserImage();

        try {
            Path path = Paths.get(fullpath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("no such image is found in folder!!");
            ex.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }


        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());


        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);

        PageableResponse<UserDto> response = Helper.getPagebleResponse(page, UserDto.class);

        return response;
    }



    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found by given id!!"));
        UserDto userDto = entityTodto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found by email!!"));

        return entityTodto(user);
    }

    @Override
    public List<UserDto> searchUsers(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoduser = users.stream().map(user -> entityTodto(user)).collect(Collectors.toList());
        return dtoduser;
    }


//user to dto
    private User dtoToentity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .password(userDto.getPassword())
//                .email(userDto.getEmail())
//                .userImage(userDto.getUserImage())
//                .build();

        return mapper.map(userDto, User.class );

    }

    //dto to user
    private UserDto entityTodto(User savedUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .password(savedUser.getPassword())
//                .email(savedUser.getEmail())
//                .userImage(savedUser.getUserImage())
//                .build();
        return mapper.map(savedUser, UserDto.class);
    }
}
