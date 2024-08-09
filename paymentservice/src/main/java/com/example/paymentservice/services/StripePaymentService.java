package com.example.paymentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.paymentservice.models.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentLinkCreateParams.AfterCompletion.Redirect;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PaymentLinkCreateParams.AfterCompletion.Type;

@Primary
@Service
public class StripePaymentService implements PaymentService {

    @Value("${stripe.api_key}")
    private String stripeKey;
    private RestTemplate restTemplate;
    private String orderUrl = "http://localhost:5001/orders/";

    @Autowired
    public StripePaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String createPaymentLink(Long orderId) {
        // TODO Auto-generated method stub
        // Order order = orderService.getOrder(orderId);
        Order order = getOrder(orderId);
        Stripe.apiKey = stripeKey;
        String url;
        try {
            url = getPaymentLink(order);
            return url;
        } catch (StripeException e) {
            throw new RuntimeException(e.getMessage());
        }        
    }

    @Override
    public String getPaymentStatus(String id) {
        // TODO Auto-generated method stub
        try {
            Session session = getSessionObject(id);
            return session.getPaymentStatus();
        } catch (StripeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Unknown";
        }
    }

    private Session getSessionObject(String sessionId) throws StripeException {

        Session session = Session.retrieve(sessionId);
        System.out.println(session);
        return session;
    }

    // private PaymentIntent getPaymentIntent(String id) {
    //     PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
    // }

    private Order getOrder(Long orderId) { 
        String url = orderUrl + orderId;
        System.out.println(url);
        ResponseEntity<Order> response = restTemplate.getForEntity(url, Order.class);
        return response.getBody();
    }

    private String getPaymentLink(Order order) throws StripeException {
        Price priceObj = getPriceObject(order);
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
                        .setUrl("http://localhost:5000/payments/done?payment_intent={CHECKOUT_SESSION_ID}")
                        .build()
                )
                .build()
            )
            .build();
        PaymentLink paymentLink = PaymentLink.create(params);
        return paymentLink.getUrl();
    }


    private Price getPriceObject(Order order) throws StripeException {
        PriceCreateParams params =
            PriceCreateParams.builder()
                .setCurrency("usd")
                .setUnitAmount(100L)
                .setProductData(
                    PriceCreateParams.ProductData.builder()
                    .setName("Order #" + order.getId())
                    .build()
                )
                .build();
        Price price = Price.create(params);
        return price;
    }

    // private Product getProductObject(Order order) throws StripeException {

    //     ProductCreateParams params = 
    //         ProductCreateParams.builder()
    //             .setName("Order #" + order.getId())
    //             .build();
        
    //     Product product = Product.create(params);
    //     return product;
    // }
}
