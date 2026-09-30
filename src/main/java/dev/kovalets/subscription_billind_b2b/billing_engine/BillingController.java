package dev.kovalets.subscription_billind_b2b.billing_engine;

import dev.kovalets.subscription_billind_b2b.billing_engine.dto.BillingReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/billing")
public class BillingController {
    private static final Logger log = LoggerFactory.getLogger(BillingController.class);
    private final BillingScheduler billingScheduler;


    public BillingController(BillingScheduler billingScheduler) {
        this.billingScheduler = billingScheduler;
    }

    @PostMapping("/triger")
    public ResponseEntity<BillingReportDto> billingTest(){
        log.info("Manual billing process triggered by admin.");

        BillingReportDto billingReport = billingScheduler.runDailyBilling();

        log.info("Billing finished. Processed: {}, Success: {}, Failed: {}",
                billingReport.totalProcessed(), billingReport.totalSuccessful(), billingReport.failedPayments());

        return ResponseEntity.ok(billingReport);
    }
}
