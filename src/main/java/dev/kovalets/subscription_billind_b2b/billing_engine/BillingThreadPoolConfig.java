package dev.kovalets.subscription_billind_b2b.billing_engine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BillingThreadPoolConfig {
    private static final int MAX_OF_ACTIVE_TASK = 10;

    @Bean("executor-service")
    public ExecutorService billingExecutor(){
        return Executors.newFixedThreadPool(MAX_OF_ACTIVE_TASK);
    }
}
