package dev.kovalets.subscription_billind_b2b.tenants;

import dev.kovalets.subscription_billind_b2b.tenants.dto.CreateTenantDto;
import dev.kovalets.subscription_billind_b2b.tenants.dto.TenantDto;
import dev.kovalets.subscription_billind_b2b.tenants.dto.UpdateTenantNameDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {
    private static final Logger log = LoggerFactory.getLogger(TenantController.class);
    private final TenantService service;

    public TenantController(TenantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TenantDto>> getAllTenants(){

        log.info("Called getAllTenants().");

        List<TenantDto> tenants = service.getAll();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDto> getTenantById(@PathVariable Long id){

        log.info("Called getTenantById(Long id) with id={}.", id);

        TenantDto tenant = service.getById(id);
        return ResponseEntity.ok(tenant);
    }

    @PostMapping
    public ResponseEntity<TenantDto> createTenant(@Valid @RequestBody CreateTenantDto tenantToCreate){

        log.info("Called createTenant(CreateTenantDto tenantToCreate) with tenantToCreate:{}", tenantToCreate.toString());

        TenantDto createdTenant = service.create(tenantToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTenant);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TenantDto> updateTenantName(@Valid @PathVariable Long id, @RequestBody UpdateTenantNameDto tenantToUpdate){

        log.info("Called updateTenantName(CreateTenantDto tenantToUpdate) with tenantToUpdate:{}", tenantToUpdate.toString());

        TenantDto updatedTenant = service.updateName(id, tenantToUpdate);
        return ResponseEntity.ok(updatedTenant);
    }
}
