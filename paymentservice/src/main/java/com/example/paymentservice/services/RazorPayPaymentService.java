package com.example.paymentservice.services;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.paymentservice.models.Order;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorPayPaymentService implements PaymentService {

    private RazorpayClient razorpayClient;
    private RestTemplate restTemplate;
    private String orderUrl = "http://localhost:5001/orders/";

    public RazorPayPaymentService(RazorpayClient razorpayClient, RestTemplate restTemplate) {
        this.razorpayClient = razorpayClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public String createPaymentLink(Long orderId) {
        // TODO Auto-generated method stub
        try {
            Order order = getOrder(orderId);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalPrice() * 100);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("accept_partial", false);
            paymentLinkRequest.put("expire_by", System.currentTimeMillis() + 15 * 60 * 1000);
            paymentLinkRequest.put("reference_id", orderId);
            paymentLinkRequest.put("description", "Payment for order no: " + orderId);

            JSONObject customer = new JSONObject();
            customer.put("name", order.getUserName());
            customer.put("email", order.getEmail());

            paymentLinkRequest.put("customer", customer);

            paymentLinkRequest.put("callback_url", "http://localhost:5000/payments/done");
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
            return payment.get("short_url");

        } catch (Exception e) {
            return "Order failed";
        }
    }

    private Order getOrder(Long orderId) { 
        String url = orderUrl + orderId;
        System.out.println(url);
        ResponseEntity<Order> response = restTemplate.getForEntity(url, Order.class);
        return response.getBody();
    }

    @Override
    public String getPaymentStatus(String paymentId) {
        // TODO Auto-generated method stub
        Payment payment;
        try {
            payment = razorpayClient.payments.fetch(paymentId);
            String status = payment.get("status");
            return status;
        } catch (RazorpayException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Unavailable";
        }
        
    }
    
}
