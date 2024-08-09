package com.example.paymentservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.paymentservice.services.PaymentService;
import com.stripe.model.Event;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}")
    public String createPaymentLink(@PathVariable("orderId") Long orderId) {
        return paymentService.createPaymentLink(orderId);
    }

    @GetMapping("/done")
    public String callbackUrl() {
        return "Done";
    }

    @PostMapping("/webhook")
    public void handleWebhookEvent(@RequestBody Event webhookEvent) {
        System.out.println(webhookEvent); 
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<String> getPaymentStatus(@PathVariable("paymentId") String paymentId) {
        String paymentStatus = paymentService.getPaymentStatus(paymentId);
        return new ResponseEntity<>(paymentStatus, HttpStatus.OK);
    }
}
