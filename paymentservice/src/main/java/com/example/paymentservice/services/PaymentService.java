package com.example.paymentservice.services;

public interface PaymentService {
    
    String createPaymentLink(String orderId);

    String getPaymentStatus(String paymentId);
}
