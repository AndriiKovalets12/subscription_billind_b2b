package dev.kovalets.subscription_billind_b2b.subscriptions;

import dev.kovalets.subscription_billind_b2b.customers.CustomerEntity;
import dev.kovalets.subscription_billind_b2b.customers.CustomerRepository;
import dev.kovalets.subscription_billind_b2b.subscription_plans.SubscriptionPlanEntity;
import dev.kovalets.subscription_billind_b2b.subscription_plans.SubscriptionPlanRepository;
import dev.kovalets.subscription_billind_b2b.subscriptions.dto.CreateSubscriptionDto;
import dev.kovalets.subscription_billind_b2b.subscriptions.dto.SubscriptionDto;
import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import dev.kovalets.subscription_billind_b2b.tenants.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final TenantRepository tenantRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final CustomerRepository customerRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               TenantRepository tenantRepository,
                               SubscriptionPlanRepository subscriptionPlanRepository,
                               CustomerRepository customerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.tenantRepository = tenantRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.customerRepository = customerRepository;
    }

    public List<SubscriptionDto> getAllActive() {
        List<SubscriptionEntity> entities = subscriptionRepository.findAllByStatus(SubscriptionStatus.ACTIVE);
        return mapperToDto(entities);
    }


    public SubscriptionDto getById(Long id) {
        SubscriptionEntity entity = subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscriptions with id=" + id + " not found."));

        return mapperToDto(entity);
    }

    @Transactional
    public SubscriptionDto create(@Valid CreateSubscriptionDto subscriptionToCreate) {
        Long targetTenantId = subscriptionToCreate.tenantId();

        TenantEntity tenant = tenantRepository
                .findById(targetTenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found."));

        CustomerEntity customer = customerRepository
                .findByIdAndTenantIdAndIsActiveTrue(subscriptionToCreate.customerId(), targetTenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found or access denied for this tenant."));

        SubscriptionPlanEntity subscriptionPlan = subscriptionPlanRepository
                .findByIdAndTenantIdAndIsActiveTrue(subscriptionToCreate.subscriptionPlanId(), targetTenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscription plan not found or access denied for this tenant."));

        SubscriptionEntity createdSubscription = new SubscriptionEntity(
                subscriptionPlan,
                customer,
                subscriptionToCreate.nextBillingDate(),
                tenant);

        subscriptionRepository.save(createdSubscription);
        return mapperToDto(createdSubscription);
    }

    @Transactional
    public void cancel(Long id, Long tenantId) {
        SubscriptionEntity entity = subscriptionRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscription with id=" + id + " not found or access denied for tenantId=" + tenantId));

        if (!entity.getStatus().equals(SubscriptionStatus.ACTIVE)) {
            throw new IllegalStateException("You can only cancel an active subscription");
        }

        entity.cancel();
    }

    private SubscriptionDto mapperToDto(SubscriptionEntity entity){
        return new SubscriptionDto(
                entity.getId(),
                entity.getSubscriptionPlan().getId(),
                entity.getCustomer().getId(),
                entity.getStartDate(),
                entity.getNextBillingDate(),
                entity.getStatus(),
                entity.getTenant().getId());
    }

    private List<SubscriptionDto> mapperToDto(List<SubscriptionEntity> entities){
        return entities.stream().map(this::mapperToDto).toList();
    }
}
