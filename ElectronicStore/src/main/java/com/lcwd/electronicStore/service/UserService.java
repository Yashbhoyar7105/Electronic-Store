package com.lcwd.electronicStore.service;

import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.dtos.UserDto;
import com.lcwd.electronicStore.entity.User;

import java.util.List;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId);

    // get all user
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //serach bye id
    UserDto getUserById(String UserId);

    //search user by email
    UserDto getUserByEmail(String email);

    //serch user
    List<UserDto> searchUsers(String keyword);

}
