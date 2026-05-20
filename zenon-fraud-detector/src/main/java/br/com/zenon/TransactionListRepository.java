package br.com.zenon;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository {
    private final List<Transaction> transactions;
    public TransactionListRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Optional<Transaction> findByOriginCustomerName(String originCustomerName) {
        return transactions.stream()
                .filter(transaction -> transaction.origin().name().equals(originCustomerName))
                .findFirst();
    }
}
