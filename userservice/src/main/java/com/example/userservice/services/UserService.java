package com.example.userservice.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dtos.SendEmailEventDTO;
import com.example.userservice.dtos.UserSignUpDTO;
import com.example.userservice.exceptions.UserDoesNotExistException;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class UserService {
    
    private TokenService tokenService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public UserService(TokenService tokenService, 
                       UserRepository userRepository, 
                       BCryptPasswordEncoder bCryptPasswordEncoder, 
                       KafkaTemplate<String, String> kafkaTemplate,
                       ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUser(Long id) throws UserDoesNotExistException {
        User user = findUserById(id, "get");
        return user;
    }

    public User addUser(String fullName, String email, String password) {
        
        User existingUser = findUserByEmail(email);
        if (existingUser != null) {
            return null;
        }
        
        User user = new User();
        user.setEmail(email);
        user.setName(fullName);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setIsVerified(false);

        User savedUser = saveUser(user);

        SendEmailEventDTO sendEmailEvent = new SendEmailEventDTO();
        sendEmailEvent.setTo(email);
        sendEmailEvent.setFrom("awskiranacd@gmail.com");
        sendEmailEvent.setSubject("Welcome to EcomStore");
        sendEmailEvent.setBody(
            "Thanks for signing up at EcomStore."
            );

        try {
            kafkaTemplate.send(
            "sendEmail",
            objectMapper.writeValueAsString(sendEmailEvent)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        
        return savedUser;
    }

    public Token loginUser(String email, String password) {
        
        User user = findUserByEmail(email);
        
        if (user == null) {
            // throw exception
            return null;
        }

        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            // throw exception
            return null;
        }

        Token savedToken = tokenService.createToken(user);
        return savedToken;
    }

    public boolean logoutUser(String token) {
        boolean isDeleted = tokenService.deleteToken(token);
        return isDeleted;
    }

    public User updateUser(Long id, User user) throws UserDoesNotExistException {
        User savedUser = findUserById(id, "update");
        if (savedUser == null) return saveUser(user);
        if (user.getName() != null) savedUser.setName(user.getName());
        if (user.getEmail() != null) savedUser.setEmail(user.getEmail());
        if (user.getHashedPassword() != null) savedUser.setHashedPassword(user.getHashedPassword());
        return saveUser(savedUser);
    }

    public User deleteUser(Long id) {
        return null;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id, String reason) throws UserDoesNotExistException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            if (reason != "update") throw new UserDoesNotExistException("User with id:" + id + " does not exist.");
            return null;
        }
        return userOptional.get();
    }

    public User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return null;
        }
        return userOptional.get();
    }

    public Token getToken(User user) {
        Token token = new Token();
        token.setUser(user);
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);

        // Convert LocalDate to Date
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        token.setExpiresAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        return token;
    }
}
