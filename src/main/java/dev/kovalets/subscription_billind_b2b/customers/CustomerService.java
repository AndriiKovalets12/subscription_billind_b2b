package dev.kovalets.subscription_billind_b2b.customers;

import dev.kovalets.subscription_billind_b2b.customers.dto.CreateCustomerDto;
import dev.kovalets.subscription_billind_b2b.customers.dto.CustomerDto;
import dev.kovalets.subscription_billind_b2b.customers.dto.SyncCustomerDto;
import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import dev.kovalets.subscription_billind_b2b.tenants.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final TenantRepository tenantRepository;

    public CustomerService(CustomerRepository customerRepository, TenantRepository tenantRepository) {
        this.customerRepository = customerRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<CustomerDto> getAll(){
        return mapperToDto(customerRepository.findAllActive());
    }

    public CustomerDto getById(Long id){
        CustomerEntity customer = customerRepository.findActiveById(id)
                .orElseThrow(()->new EntityNotFoundException("Customer with id=" + id + "not found."));
        return mapperToDto(customer);
    }

    public CustomerDto create(CreateCustomerDto customerToCreate){
        TenantEntity tenant = tenantRepository
                .findById(customerToCreate.tenantId())
                .orElseThrow(()->new EntityNotFoundException("Tenant with id=" + customerToCreate.tenantId() + " not found."));

        CustomerEntity createdCustomer = new CustomerEntity(
                customerToCreate.firstName(),
                customerToCreate.lastName(),
                customerToCreate.email(),
                customerToCreate.customerExternalId(),
                tenant
        );

        customerRepository.save(createdCustomer);
        return mapperToDto(createdCustomer);
    }

    @Transactional
    public CustomerDto sync(Long tenantId, String externalCustomerId, SyncCustomerDto syncDto) {
        // 1. Шукаємо НЕЗАЛЕЖНО від прапорця isActive, щоб знайти навіть архівного клієнта
        CustomerEntity customer = customerRepository
                .findByExternalCustomerIdAndTenantId(externalCustomerId, tenantId)
                .orElse(null);

        TenantEntity tenant = tenantRepository
                .findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with id=" + tenantId + " not found."));

        if (customer == null) {
            // Створюємо нового (isActive = true за замовчуванням)
            customer = new CustomerEntity(
                    syncDto.firstName(),
                    syncDto.lastName(),
                    syncDto.email(),
                    externalCustomerId,
                    tenant
            );
        } else {
            // Якщо клієнт був логічно видалений, але від B2B-партнера знову прийшли дані — реактивуємо його!
            if (!customer.isActive()) {
                customer.activate();
            }
            customer.updateProfile(syncDto.firstName(), syncDto.lastName(), syncDto.email());
        }

        customer = customerRepository.save(customer);
        return mapperToDto(customer);
    }


    @Transactional
    public void deleteByExternalId(Long tenantId, String externalCustomerId) {
        CustomerEntity customer = customerRepository
                .findByExternalCustomerIdAndTenantId(externalCustomerId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with tenantId=" + tenantId + " and externalCustomerId=" + externalCustomerId));

        customer.deactivate();
        customerRepository.save(customer);
    }

    private CustomerDto mapperToDto(CustomerEntity entity){
        return new CustomerDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getExternalCustomerId(),
                entity.getTenant().getId());
    }

    private List<CustomerDto> mapperToDto(List<CustomerEntity> entities){
        return entities.stream().map(this::mapperToDto).toList();
    }
}

