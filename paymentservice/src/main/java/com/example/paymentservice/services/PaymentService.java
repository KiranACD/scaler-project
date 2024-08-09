package com.example.paymentservice.services;

public interface PaymentService {
    
    String createPaymentLink(Long orderId);

    String getPaymentStatus(String id);
}
