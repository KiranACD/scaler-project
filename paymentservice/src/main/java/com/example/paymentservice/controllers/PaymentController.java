package com.example.paymentservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentservice.dtos.CreatePaymentLinkRequestDTO;
import com.example.paymentservice.services.PaymentService;
import com.stripe.model.Event;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}")
    public String createPaymentLink(@PathVariable("orderId") String orderId) {
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

}
