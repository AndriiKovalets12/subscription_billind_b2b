package dev.kovalets.subscription_billind_b2b.invoices;

import dev.kovalets.subscription_billind_b2b.invoices.dto.CreateInvoiceDto;
import dev.kovalets.subscription_billind_b2b.invoices.dto.InvoiceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);
    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAllInvoices(){
        log.info("Called getAllInvoices().");

        List<InvoiceDto> invoices = service.getAll();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long id){
        log.info("Called getInvoiceById(Long id) with id={}", id);

        InvoiceDto invoice = service.getById(id);
        return ResponseEntity.ok(invoice);
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody CreateInvoiceDto invoiceToCreate){
        log.info("Called createInvoice(CreateInvoiceDto invoiceToCreate) with invoiceToCreate:{}", invoiceToCreate.toString());

        InvoiceDto createdInvoice = service.create(invoiceToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }
}
