package com.ecommerce.shop.service;

import com.ecommerce.shop.dtos.StripePaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeServiceI {
    PaymentIntent createPaymentIntent(StripePaymentDto stripePaymentDto) throws StripeException;
}
