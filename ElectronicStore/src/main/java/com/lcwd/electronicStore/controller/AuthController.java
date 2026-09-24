package com.lcwd.electronicStore.controller;


import com.lcwd.electronicStore.dtos.JwtRequest;
import com.lcwd.electronicStore.dtos.JwtResponse;
import com.lcwd.electronicStore.dtos.UserDto;
import com.lcwd.electronicStore.exception.BadApiRequestException;
import com.lcwd.electronicStore.security.JWTHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager manager;

    private Logger logger= LoggerFactory.getLogger(AuthController.class);



    @Autowired
    private JWTHelper jwtHelper;

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name),UserDto.class), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthentication(request.getEmail(),request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);
        logger.info("token generated : {}", token);


        UserDto userDto=modelMapper.map(userDetails,UserDto.class);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto).build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void doAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(email,password);
        try{
            manager.authenticate(authentication);

        }catch (BadCredentialsException e){
            throw new BadApiRequestException("Invalid username or password!! ");
        }


    }
}
