package dev.kovalets.subscription_billind_b2b.tenants;

import dev.kovalets.subscription_billind_b2b.tenants.dto.CreateTenantDto;
import dev.kovalets.subscription_billind_b2b.tenants.dto.TenantDto;
import dev.kovalets.subscription_billind_b2b.tenants.dto.UpdateTenantNameDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {
    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<TenantDto> getAll() {
        List<TenantEntity> tenantEntities = tenantRepository.findAll();
        return mapperToDto(tenantEntities);
    }

    public TenantDto getById(Long id) {
        TenantEntity tenantEntity = tenantRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with id=" + id + " not found."));

        return mapperToDto(tenantEntity);
    }

    private TenantDto mapperToDto(TenantEntity entity){
        return new TenantDto(
                entity.getId(),
                entity.getName());
    }

    private List<TenantDto> mapperToDto(List<TenantEntity> entities){
        return entities.stream().map(this::mapperToDto).toList();
    }

    @Transactional
    public TenantDto create(CreateTenantDto tenantToCreate) {
        if (!tenantRepository.existsByName(tenantToCreate.name())){

            TenantEntity tenant = new TenantEntity(tenantToCreate.name());
            tenantRepository.save(tenant);

            return mapperToDto(tenant);
        } else {
            throw new EntityExistsException("Tenant with this name already exists.");
        }
    }

    @Transactional
    public TenantDto updateName(Long id, UpdateTenantNameDto tenantToUpdate) {
        TenantEntity tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with id=" + id + " not found."));

        String newName = tenantToUpdate.name();

        if (tenant.getName().equals(newName)) {
            return mapperToDto(tenant);
        }

        if (tenantRepository.existsByName(newName)) {
            throw new EntityExistsException("Tenant with the name '" + newName + "' already exists.");
        }

        tenant.updateName(newName);

        return mapperToDto(tenant);
    }
}
