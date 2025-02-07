package aggregator.service;

import aggregator.entity.Transaction;
import aggregator.entity.TransactionTimestampComperator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionService {

    private final TransactionFetchService transactionFetchService;

    public TransactionService(TransactionFetchService transactionFetchService) {
        this.transactionFetchService = transactionFetchService;
    }

    @Cacheable(value = "transactions", key = "#account")
    public List<Transaction> aggregateTransactions(String account) {
        String url1 = "http://localhost:8888/transactions?account=" + account;
        String url2 = "http://localhost:8889/transactions?account=" + account;

        CompletableFuture<List<Transaction>> future1 = transactionFetchService.fetchTransactionsAsync(url1);
        CompletableFuture<List<Transaction>> future2 = transactionFetchService.fetchTransactionsAsync(url2);

        CompletableFuture.allOf(future1, future2).join();

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(future1.join());
        allTransactions.addAll(future2.join());

        allTransactions.sort(new TransactionTimestampComperator());

        return allTransactions;
    }
}