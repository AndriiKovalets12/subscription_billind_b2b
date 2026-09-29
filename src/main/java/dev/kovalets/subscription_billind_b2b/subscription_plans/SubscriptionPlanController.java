package dev.kovalets.subscription_billind_b2b.subscription_plans;

import dev.kovalets.subscription_billind_b2b.subscription_plans.dto.CreateSubscriptionPlanDto;
import dev.kovalets.subscription_billind_b2b.subscription_plans.dto.SubscriptionPlanDto;
import dev.kovalets.subscription_billind_b2b.subscription_plans.dto.UpdateSubscriptionPlanDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class SubscriptionPlanController {
    private final SubscriptionPlanService service;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SubscriptionPlanController.class);

    public SubscriptionPlanController(SubscriptionPlanService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDto>> getAllSubPlans(){

        log.info("Called getAllSubPlans().");

        List<SubscriptionPlanDto> planDtoList = service.getAll();
        return ResponseEntity.ok(planDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDto> getSubPlanById(@PathVariable Long id){

        log.info("Called getSubPlanById(Long id) with id={}", id);

        SubscriptionPlanDto subPlanDto = service.getById(id);
        return ResponseEntity.ok(subPlanDto);
    }

    @PostMapping
    public ResponseEntity<SubscriptionPlanDto> createSubPlan(
            @Valid @RequestBody CreateSubscriptionPlanDto subPlanToCreate){

        log.info("Called createSubPlan(CreateSubscriptionPlanDto subPlanToCreate) with " +
                "subPlanToCreate:{}", subPlanToCreate.toString());

        SubscriptionPlanDto newSubPlan = service.create(subPlanToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSubPlan);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDto> updateSubPlanName(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubscriptionPlanDto subPlanToUpdate){

        log.info("Called updateSubPlan(Long id, UpdateSubscriptionPlanDto subPlanToUpdate) with " +
                "id={}, subPlanToUpdate:{}", id, subPlanToUpdate.toString());

        SubscriptionPlanDto updatedSubPlan = service.updateName(id, subPlanToUpdate);
        return ResponseEntity.ok(updatedSubPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDto> deleteSubPlan(@PathVariable Long id){

        log.info("Called deleteSubPlan(Long id) with id={}", id);

        SubscriptionPlanDto deletedSubPlan = service.delete(id);
        return ResponseEntity.ok(deletedSubPlan);
    }
}
