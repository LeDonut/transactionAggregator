package aggregator.service;

import aggregator.entity.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionFetchService {

    private final RestTemplate restTemplate;

    public TransactionFetchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<List<Transaction>> fetchTransactionsAsync(String url) {
        for (int i = 0; i < 5; i++) {
            try {
                ResponseEntity<Transaction[]> response = restTemplate.getForEntity(url, Transaction[].class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    Transaction[] transactions = response.getBody();
                    return CompletableFuture.completedFuture(
                            transactions != null ? Arrays.asList(transactions) : Collections.emptyList()
                    );
                }

                if (!Arrays.asList(529, 503).contains(response.getStatusCode().value())) {
                    return CompletableFuture.completedFuture(Collections.emptyList());
                }

                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("Fehler beim Abrufen der API: " + url);
            }
        }

        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}