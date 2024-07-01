package com.example.paymentservice.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.paymentservice.models.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.PaymentLink.AfterCompletion;
import com.stripe.param.PaymentLinkCreateParams.AfterCompletion.Redirect;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PaymentLinkCreateParams.AfterCompletion.Type;

@Primary
@Service
public class StripePaymentService implements PaymentService {

    @Value("${stripe.api_key}")
    private String stripeKey;
    private OrderService orderService;

    @Autowired
    public StripePaymentService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String createPaymentLink(String orderId) {
        // TODO Auto-generated method stub
        // Order order = orderService.getOrder(orderId);
        Stripe.apiKey = stripeKey;
        String url;
        try {
            url = getPaymentLink(null);
            return url;
        } catch (StripeException e) {
            throw new RuntimeException(e.getMessage());
        }        
    }

    @Override
    public String getPaymentStatus(String paymentId) {
        // TODO Auto-generated method stub
        return null;
    }

    private String getPaymentLink(Order order) throws StripeException {
        Price priceObj = getPriceObject();
        PaymentLinkCreateParams params =
        PaymentLinkCreateParams.builder()
            .addLineItem(
            PaymentLinkCreateParams.LineItem.builder()
                .setPrice(priceObj.getId())
                .setQuantity(1L)
                .build()
            )
            .setAfterCompletion(
                PaymentLinkCreateParams.AfterCompletion.builder()
                .setType(Type.REDIRECT)
                .setRedirect(
                    Redirect.builder()
                        .setUrl("http://localhost:8080/payment/done")
                        .build()
                )
                .build()
            )
            .build();
        PaymentLink paymentLink = PaymentLink.create(params);
        return paymentLink.getUrl();
    }


    private Price getPriceObject() throws StripeException {
        PriceCreateParams params =
            PriceCreateParams.builder()
                .setCurrency("usd")
                .setUnitAmount(100L)
                .setProductData(
                    PriceCreateParams.ProductData.builder()
                    .setName("Gold Coin")
                    .build()
                )
                .build();
        Price price = Price.create(params);
        return price;
    }
}
