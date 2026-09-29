package dev.kovalets.subscription_billind_b2b.invoices;

import dev.kovalets.subscription_billind_b2b.invoices.dto.CreateInvoiceDto;
import dev.kovalets.subscription_billind_b2b.invoices.dto.InvoiceDto;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionEntity;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionRepository;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionStatus;
import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import dev.kovalets.subscription_billind_b2b.tenants.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jdk.jshell.JShell;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TenantRepository tenantRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          SubscriptionRepository subscriptionRepository,
                          TenantRepository tenantRepository) {
        this.invoiceRepository = invoiceRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<InvoiceDto> getAll() {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findAll();
        return mapperToDto(invoiceEntities);
    }

    public InvoiceDto getById(Long id){
        InvoiceEntity invoice = invoiceRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice with id=" + id + " not found."));

        return mapperToDto(invoice);
    }

    public InvoiceDto create(@Valid CreateInvoiceDto invoiceToCreate){
        Long tenantId = invoiceToCreate.tenantId();
        Long subscriptionId = invoiceToCreate.subscriptionId();

        TenantEntity tenant = tenantRepository
                .findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with id=" + tenantId + " not found."));

        SubscriptionEntity subscription = subscriptionRepository
                .findByIdAndTenantId(subscriptionId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription with id=" + subscriptionId + " and with tenant_id=" + tenantId + " not found."));

        InvoiceEntity createdInvoice = new InvoiceEntity(
                subscription,
                invoiceToCreate.amount(),
                invoiceToCreate.status(),
                invoiceToCreate.billingPeriodStart(),
                invoiceToCreate.billingPeriodEnd(),
                1L,
                invoiceToCreate.idempotencyKey(),
                tenant
        );

        invoiceRepository.save(createdInvoice);
        return mapperToDto(createdInvoice);
    }

    private InvoiceDto mapperToDto(InvoiceEntity entity){
        return new InvoiceDto(
                entity.getId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getBillingPeriodStart(),
                entity.getBillingPeriodEnd(),
                entity.getTenant().getId());
    }

    private List<InvoiceDto> mapperToDto(List<InvoiceEntity> entities){
        return entities.stream().map(this::mapperToDto).toList();
    }
}
