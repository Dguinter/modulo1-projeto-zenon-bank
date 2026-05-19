package br.com.zenon;

import java.math.BigDecimal;

public record Transaction(int step, TransactionType type, BigDecimal amount, TransactionCustomer origin,
                          TransactionCustomer recipient, boolean isFraud, boolean isFlaggedFraud) {
    public Transaction {
        if (step <= 0) throw new  IllegalArgumentException("O valor de step deve ser positivo: " + step);
        if (amount.signum() <= 0) throw new  IllegalArgumentException("O valor de amount deve ser positivo: " + amount);
    }
}

