package aggregator.controller;

import aggregator.entity.Transaction;
import aggregator.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aggregate")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> aggregateTransactions(@RequestParam("account") String account) {
        List<Transaction> transactions = transactionService.aggregateTransactions(account);

        return ResponseEntity.ok(transactions);
    }
}
