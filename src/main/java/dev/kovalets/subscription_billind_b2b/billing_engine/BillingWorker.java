package dev.kovalets.subscription_billind_b2b.billing_engine;

import dev.kovalets.subscription_billind_b2b.billing_engine.dto.BillingResult;
import dev.kovalets.subscription_billind_b2b.invoices.InvoiceEntity;
import dev.kovalets.subscription_billind_b2b.invoices.InvoiceRepository;
import dev.kovalets.subscription_billind_b2b.invoices.InvoiceService;
import dev.kovalets.subscription_billind_b2b.invoices.InvoiceStatus;
import dev.kovalets.subscription_billind_b2b.invoices.dto.CreateInvoiceDto;
import dev.kovalets.subscription_billind_b2b.payments.PaymentGateway;
import dev.kovalets.subscription_billind_b2b.subscription_plans.BillingCycle;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionEntity;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionRepository;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.time.OffsetDateTime;

@Component
public class BillingWorker {
    private static final Logger log = LoggerFactory.getLogger(BillingWorker.class)
            ;
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    private final PaymentGateway paymentGateway;
    private final SubscriptionRepository subscriptionRepository;

    public BillingWorker (InvoiceService invoiceService,
                          InvoiceRepository invoiceRepository,
                          PaymentGateway paymentGateway,
                          SubscriptionRepository subscriptionRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.paymentGateway = paymentGateway;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BillingResult processSubscription(SubscriptionEntity subscription) {
        String idempotencyKey = String.format("sub_%d_%s",
                subscription.getId(),
                subscription.getNextBillingDate().toLocalDate().toString());
        try {
            OffsetDateTime currentPeriodStart = subscription.getNextBillingDate();
            OffsetDateTime currentPeriodEnd = computeBillingPeriodEnd(currentPeriodStart, subscription.getSubscriptionPlan().getDuration());

            CreateInvoiceDto invoiceToCreate = new CreateInvoiceDto(
                    subscription.getId(),
                    subscription.getSubscriptionPlan().getCost(),
                    InvoiceStatus.PENDING,
                    currentPeriodStart,
                    currentPeriodEnd,
                    idempotencyKey,
                    subscription.getTenant().getId()
            );

            InvoiceEntity createdInvoice = invoiceService.createAndReturnEntity(invoiceToCreate);

            if (paymentGateway.processTransaction()) {
                createdInvoice.setStatus(InvoiceStatus.PAID);
                subscription.activate();
                subscription.resetPaymentAttempt();
                subscription.setNextBillingDate(currentPeriodEnd);

                invoiceRepository.saveAndFlush(createdInvoice);
                subscriptionRepository.saveAndFlush(subscription);

                return new BillingResult(subscription.getId(), createdInvoice.getId(), InvoiceStatus.PAID, "Success.");
            } else {

                createdInvoice.setStatus(InvoiceStatus.FAILED);
                if (subscription.incrementPaymentAttempt()) {
                    subscription.markAsPastDue();
                    subscription.updateNextRetryDate(currentPeriodStart);

                } else {
                    subscription.cancel();

                    invoiceRepository.saveAndFlush(createdInvoice);
                    subscriptionRepository.saveAndFlush(subscription);

                }
                return new BillingResult(subscription.getId(), createdInvoice.getId(), InvoiceStatus.FAILED, "Payment failed.");
            }

            } catch(ObjectOptimisticLockingFailureException | OptimisticLockException e){
                log.warn("Concurrency conflict for subscription {}. Rolling back.", subscription.getId());
                return new BillingResult(subscription.getId(), null, null, "Skipped: Duplicate idempotency key");
            } catch(Exception e){
                log.error("Failed to process billing for subscription {}", subscription.getId(), e);
                throw new RuntimeException("Billing process failed", e);
            }
    }

    private OffsetDateTime computeBillingPeriodEnd(OffsetDateTime currentPeriodStart, BillingCycle duration){
        if (duration.equals(BillingCycle.WEEKLY)) return currentPeriodStart.plusWeeks(1);
        else if (duration.equals(BillingCycle.MONTHLY)) return currentPeriodStart.plusMonths(1);
        else if (duration.equals(BillingCycle.QUARTERLY)) return currentPeriodStart.plusMonths(3);
        else if (duration.equals(BillingCycle.YEARLY)) return currentPeriodStart.plusYears(1);
        else throw new IllegalArgumentException("Constant named as " + duration + " not in BillingCycle enum.");
    }
}
