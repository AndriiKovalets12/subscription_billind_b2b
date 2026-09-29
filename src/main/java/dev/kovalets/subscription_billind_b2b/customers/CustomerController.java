package dev.kovalets.subscription_billind_b2b.customers;

import dev.kovalets.subscription_billind_b2b.customers.dto.CreateCustomerDto;
import dev.kovalets.subscription_billind_b2b.customers.dto.CustomerDto;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService service;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(){

        log.info("Called getAllSubPlans().");

        List<CustomerDto> allCustomers = service.getAll();
        return ResponseEntity.ok(allCustomers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id){

        log.info("Called getCustomerById(Long id) with id={}", id);

        CustomerDto customerDto = service.getById(id);
        return ResponseEntity.ok(customerDto);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CreateCustomerDto customerToCreate){

        log.info("Called createCustomer(CreateCustomerDto customerToCreate) with customerToCreate={}", customerToCreate);

        CustomerDto customerDto = service.create(customerToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }

}
