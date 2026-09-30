package dev.kovalets.subscription_billind_b2b.payments;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PaymentGateway {
    private final static Random rnd = new Random();

    public PaymentGateway() {

    }

    public boolean processTransaction(){
        return rnd.nextInt(1, 100) > 3;
    }
}
