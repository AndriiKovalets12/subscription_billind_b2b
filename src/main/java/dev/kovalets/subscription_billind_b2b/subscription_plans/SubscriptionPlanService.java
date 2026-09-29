package dev.kovalets.subscription_billind_b2b.subscription_plans;

import dev.kovalets.subscription_billind_b2b.subscription_plans.dto.CreateSubscriptionPlanDto;
import dev.kovalets.subscription_billind_b2b.subscription_plans.dto.SubscriptionPlanDto;
import dev.kovalets.subscription_billind_b2b.subscription_plans.dto.UpdateSubscriptionPlanDto;
import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import dev.kovalets.subscription_billind_b2b.tenants.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final TenantRepository tenantRepository;

    public SubscriptionPlanService(SubscriptionPlanRepository subscriptionPlanRepository, TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    public List<SubscriptionPlanDto> getAll(){
        return mapperToDto(subscriptionPlanRepository.findAllByIsActiveTrue());
    }

    public SubscriptionPlanDto getById(Long id){
        SubscriptionPlanEntity entity = subscriptionPlanRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(EntityNotFoundException::new);

        return mapperToDto(entity);
    }

    @Transactional
    public SubscriptionPlanDto create(CreateSubscriptionPlanDto subPlanToCreate){
        TenantEntity tenant = tenantRepository
                .findById(subPlanToCreate.tenant_id())
                .orElseThrow(EntityNotFoundException::new);

        SubscriptionPlanEntity createdSubPlan =
                new SubscriptionPlanEntity(
                        subPlanToCreate.name(),
                        subPlanToCreate.cost(),
                        subPlanToCreate.duration(),
                        tenant,
                        true
                );

        subscriptionPlanRepository.save(createdSubPlan);
        return mapperToDto(createdSubPlan);
    }

    @Transactional
    public SubscriptionPlanDto updateName(Long id, UpdateSubscriptionPlanDto subPlanToUpdate){

        SubscriptionPlanEntity entity = subscriptionPlanRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(EntityNotFoundException::new);

        entity.updateName(subPlanToUpdate.name());
        return mapperToDto(entity);
    }

    @Transactional
    public SubscriptionPlanDto delete(Long id){

        SubscriptionPlanEntity entity = subscriptionPlanRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(EntityNotFoundException::new);

        entity.archive();
        return mapperToDto(entity);
    }

    private SubscriptionPlanDto mapperToDto(SubscriptionPlanEntity entity){
        return new SubscriptionPlanDto(
                entity.getId(),
                entity.getName(),
                entity.getCost(),
                entity.getDuration(),
                entity.getTenant().getId(),
                entity.isActive());
    }

    private List<SubscriptionPlanDto> mapperToDto(List<SubscriptionPlanEntity> entities){
        return entities.stream().map(this::mapperToDto).toList();
    }
}
