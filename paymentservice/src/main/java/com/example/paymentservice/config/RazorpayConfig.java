package com.example.paymentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
public class RazorpayConfig {
    @Value("${razorpay.key_id}")
    private String razorpayKeyId;
    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Bean
    public RazorpayClient createRazorpayClient() throws RazorpayException {
        return new RazorpayClient(razorpayKeyId, razorpaySecret);
    }
}
