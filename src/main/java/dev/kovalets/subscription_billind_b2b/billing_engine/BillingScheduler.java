package dev.kovalets.subscription_billind_b2b.billing_engine;

import dev.kovalets.subscription_billind_b2b.billing_engine.dto.BillingResult;
import dev.kovalets.subscription_billind_b2b.billing_engine.dto.BillingReportDto;
import dev.kovalets.subscription_billind_b2b.invoices.InvoiceStatus;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionEntity;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionRepository;
import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class BillingScheduler {
    private final SubscriptionRepository subscriptionRepository;
    private final BillingWorker billingWorker;
    private final ExecutorService billingExecutor;

    public BillingScheduler(SubscriptionRepository subscriptionRepository, BillingWorker billingWorker, ExecutorService billingExecutor) {
        this.subscriptionRepository = subscriptionRepository;
        this.billingWorker = billingWorker;
        this.billingExecutor = billingExecutor;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public BillingReportDto runDailyBilling(){
        int pageNumber = 0;
        int pageSize = 100;
        OffsetDateTime now = OffsetDateTime.now();
        Page<SubscriptionEntity> page;

        List<BillingResult> allResults = new ArrayList<>();
        do {
            page = subscriptionRepository.findActiveSubscriptionsByBilling(
                    now,
                    SubscriptionStatus.ACTIVE,
                    PageRequest.of(pageNumber, pageSize)
            );

            addToAllResults(page, allResults);

        } while (page.hasNext());

        do {
            page = subscriptionRepository.findPastDueSubscriptionsByBilling(
                    now,
                    SubscriptionStatus.PAST_DUE,
                    PageRequest.of(pageNumber, pageSize)
            );

            addToAllResults(page, allResults);

        } while (!page.isEmpty());



        return generateReport(allResults);
    }

    private void addToAllResults(Page<SubscriptionEntity> page, List<BillingResult> allResults) {
        List<CompletableFuture<BillingResult>> futures = page.getContent().stream()
                .map(sub -> CompletableFuture.supplyAsync(
                        () -> billingWorker.processSubscription(sub),
                        billingExecutor
                ))
                .toList();

        List<BillingResult> batchResults = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        allResults.addAll(batchResults);
    }

    private BillingReportDto generateReport(List<BillingResult> resultList){
        Long success = resultList.stream().filter(res -> res.status().equals(InvoiceStatus.PAID)).count();
        Long failed = resultList.stream().filter(res -> res.status().equals(InvoiceStatus.FAILED)).count();

        return new BillingReportDto((long) resultList.size(), success, failed, resultList);
    }
}
