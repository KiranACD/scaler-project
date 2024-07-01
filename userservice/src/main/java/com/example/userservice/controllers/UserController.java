package com.example.userservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dtos.UserDTO;
import com.example.userservice.dtos.UserLoginDTO;
import com.example.userservice.dtos.UserLogoutDTO;
import com.example.userservice.dtos.UserSignUpDTO;
import com.example.userservice.exceptions.UserDoesNotExistException;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.TokenService;
import com.example.userservice.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> signUp(@RequestBody UserSignUpDTO request) {
        // Here we can send a custom response when the user returned is null
        UserDTO userDTO = UserDTO.from(userService.addUser(request.getName(), request.getEmail(), request.getPassword()));
        // if (userDTO.getSuccess() == false) {
        //     return new ResponseEntity<>(userDTO, HttpStatus.BAD_REQUEST);
        // }
        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO loginDetails) {
        Token token = userService.loginUser(loginDetails.getEmail(), loginDetails.getPassword());
        return new ResponseEntity<>(token.getValue(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody UserLogoutDTO request) {
        boolean loggedOut = userService.logoutUser(request.getToken());
        if (loggedOut) {
            return new ResponseEntity<>("Logged Out", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Unable to log out", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/validate/{token}")
    public ResponseEntity<UserDTO> validateToken(@PathVariable("token") @NonNull String token) {
        User user = tokenService.validate(token);
        return new ResponseEntity<>(UserDTO.from(user), HttpStatus.OK);
    }

    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
