package dev.kovalets.subscription_billind_b2b.subscriptions;

import dev.kovalets.subscription_billind_b2b.subscriptions.dto.CreateSubscriptionDto;
import dev.kovalets.subscription_billind_b2b.subscriptions.dto.SubscriptionDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/subscriptions")
public class SubscriptionController {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllActiveSubscriptions(){
        log.info("Called getAllActiveSubscriptions().");

        List<SubscriptionDto> activeSub = service.getAllActive();
        return ResponseEntity.ok(activeSub);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Long id){
        log.info("Called getSubscriptionById(Long id) with id={}.", id);

        SubscriptionDto subscription = service.getById(id);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@Valid @RequestBody CreateSubscriptionDto subscriptionToCreate){

        log.info("Called createSubscription(CreateSubscriptionDto subscriptionToCreate) with subscriptionToCreate:{}", subscriptionToCreate.toString());

        SubscriptionDto createdSubscription = service.create(subscriptionToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long id, Long tenantId){
        log.info("Called cancelSubscription(Long id) with id={}", id);

        service.cancel(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
