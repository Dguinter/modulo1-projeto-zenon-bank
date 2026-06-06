package br.com.zenon;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public void printFraudAnalysis(List<Transaction> transactions) {
        List<Transaction> fraudulentTransactions = transactions.stream()
                .filter(Transaction::isFraud)
                .toList();

        List<Transaction> top3HighestFrauds = fraudulentTransactions.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .toList();

        List<String> top5OriginCustomerNames = fraudulentTransactions.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(5)
                .toList();

        BigDecimal totalLoss = fraudulentTransactions.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<TransactionType, Long> fraudsByTransactionType = fraudulentTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));

        IO.println("ANÁLISE DE FRAUDES");
        IO.println("Total de Fraudes: " + fraudulentTransactions.size());

        IO.println("Top 3 Fraudes de Maior Valor:");
        top3HighestFrauds.forEach(transaction ->
                IO.println(transaction.amount().toPlainString())
        );

        IO.println("Top 5 Clientes de Origem Suspeitos:");
        top5OriginCustomerNames.forEach(IO::println);

        IO.println("Prejuízo total: " + totalLoss.toPlainString());

        IO.println("Fraudes por tipo de transação:");
        fraudsByTransactionType.forEach((type, total) ->
                IO.println(type + ": " + total)
        );
    }
}