package com.example.userservice.services;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepository;

@Service
public class TokenService {

    private TokenRepository tokenRepository;
    
    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    
    public Token createToken(User user) {
        String tokenString = randomAlphabeticStringRandomInt(10);
        Token token = new Token();
        token.setUser(user);
        Date expiryDate = getExpiryDate(30);
        token.setExpiresAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        Token savedToken = saveToken(token);
        return savedToken;
    }

    public Boolean deleteToken(String tokenValue) {
        Token token = findTokenByValueAndDeleted(tokenValue);
        if (token == null) {
            return false;
        }
        token.setDeleted(true);
        saveToken(token);
        return true;
    }

    public User validate(String tokenValue) {
        Token token = findTokenByValueAndDeletedAndExpiryGreaterThan(tokenValue);
        if (token == null) {
            // throw exception
            return null;
        }
        return token.getUser();
    }   

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public Date getExpiryDate(int days) {
        LocalDate today = LocalDate.now();
        LocalDate daysLater = today.plus(days, ChronoUnit.DAYS);

        // Convert LocalDate to Date
        Date expiryDate = Date.from(daysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return expiryDate;
    }

    public Token findTokenByValueAndDeleted(String value) {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndIsDeletedEquals(value, false);
        if (tokenOptional.isEmpty()) {
            // throw exception here
            return null;
        }
        return tokenOptional.get();
    }

    public Token findTokenByValueAndDeletedAndExpiryGreaterThan(String value) {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndIsDeletedEqualsAndExpiresAtGreaterThan(value, false, new Date());
        if (tokenOptional.isEmpty()) {
            // throw exception
            return null;
        }
        return tokenOptional.get();
    }

    public String randomStringByteArrayUnbounded(int length) {
        byte[] array = new byte[length];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }

    public String randomStringBounded(int length) {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = length;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    public String randomAlphabeticStringRandomInt(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

        return generatedString;
    }

    public String randomAlphanumericStringRandomInts(int length) {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();

        return generatedString;
    }
}
