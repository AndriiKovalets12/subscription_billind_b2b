package dev.kovalets.subscription_billind_b2b.customers;

import dev.kovalets.subscription_billind_b2b.customers.dto.CustomerDto;
import dev.kovalets.subscription_billind_b2b.customers.dto.SyncCustomerDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/integrations")
public class CustomerIntegrationsController {
    private static final Logger log = LoggerFactory.getLogger(CustomerIntegrationsController.class);
    private final CustomerService service;

    public CustomerIntegrationsController(CustomerService service) {
        this.service = service;
    }

    @PutMapping("/tenants/{tenantId}/customers/{externalCustomerId}")
    public ResponseEntity<CustomerDto> syncCustomer(
            @PathVariable("tenantId") Long tenantId,
            @PathVariable("externalCustomerId") String externalCustomerId,
            @Valid @RequestBody SyncCustomerDto syncDto) {

        log.info("Called syncCostumer with tenantId={}, externalCustomerId={}, syncDto:{}", tenantId, externalCustomerId, syncDto.toString());
        CustomerDto customerDto = service.sync(tenantId, externalCustomerId, syncDto);
        return ResponseEntity.ok(customerDto);
    }

    @DeleteMapping("/tenants/{tenantId}/customers/{externalCustomerId}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable("tenantId") Long tenantId,
            @PathVariable("externalCustomerId") String externalCustomerId) {

        log.info("Called deleteCustomer() for tenantId={}, externalCustomerId={}", tenantId, externalCustomerId);

        service.deleteByExternalId(tenantId, externalCustomerId);

        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
