package com.lcwd.electronicStore.controller;


import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronicStore.dtos.JwtRequest;
import com.lcwd.electronicStore.dtos.JwtResponse;
import com.lcwd.electronicStore.dtos.UserDto;
import com.lcwd.electronicStore.entity.User;
import com.lcwd.electronicStore.exception.BadApiRequestException;
import com.lcwd.electronicStore.security.JWTHelper;
import com.lcwd.electronicStore.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Value("${googleClientId}")
    private String googleClientId;
    @Value("${newPassword}")
    private String newPassword;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager manager;

    private Logger logger= LoggerFactory.getLogger(AuthController.class);



    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserService userService;

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

    //login with google
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {

        String idToken=data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken=GoogleIdToken.parse(verifier.getJsonFactory(), idToken);

        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        logger.info("payload : {}", payload);

        String email = payload.getEmail();
        User user=null;

        user = this.userService.findUserByEmailOptional(email).orElse(null);

        if(user==null){
            user= this.saveUser(email,data.get("name").toString(),data.get("UrlPhoto").toString());
        }
        ResponseEntity<JwtResponse> jwtResponse = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());

        return jwtResponse;

    }

    private User saveUser(String email, String name, String urlPhoto) {

        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .userImage(urlPhoto)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);

        return modelMapper.map(user, User.class);
    }

}
