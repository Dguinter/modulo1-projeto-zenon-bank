package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Main {
    void main() {
       var t1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9838.64"),
        new TransactionCustomer("C1231006815", new BigDecimal("170136.0"),new BigDecimal("160296.36")),
        new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                false, false
        );

        var t2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"),new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true, false
        );

        IO.println(t1);
        IO.print(t2);

        IO.println("------------------------------------------------------------------------------");

        var transactionIngestor = new TransactionIngestor();
        var transactionReport = new TransactionReport();

        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions.size());

        transactions.forEach(IO::println);

        //String bigFileName = "data/PS_20174392719_1491204439457_log.csv";
        //transactionReport.printSummary(bigFileName);

        IO.println("------------------------------------------------------------------------------");


     var fraudAnalyzer = new FraudAnalyzer();
     fraudAnalyzer.printFraudAnalysis(transactions);
        List<Transaction> transactionsBadData = transactionIngestor.read("data/paysim_with_bad_data.csv");
        IO.println(transactionsBadData.size());

     transactionsBadData.forEach(IO::println);
     IO.println("------------------------------------------------------------------------------");


        var repository = new TransactionListRepository(transactions);
        var transactionFound = repository.findByOriginCustomerName("C212413768");

        transactionFound.ifPresentOrElse(
                transaction -> IO.println("Transação encontrada por cliente de origem: " + transaction),
                () -> IO.println("Transação não encontrada por cliente de origem")
        );
        IO.println("------------------------------------------------------------------------------");

        long startTime = System.nanoTime();

        var lastTransactionFound = repository.findByOriginCustomerName("C1868032458");

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        lastTransactionFound.ifPresent(IO::println);
        IO.println("Tempo de busca em nanossegundos: " + duration);
        Map<String, Transaction> transactionsByOriginName = new HashMap<>();
        for (Transaction transaction : transactions) {
            transactionsByOriginName.put(transaction.origin().name(), transaction);
        }
        long mapStartTime = System.nanoTime();
        var transactionFoundByMap = transactionsByOriginName.get("C1868032458");
        long mapEndTime = System.nanoTime();
        long mapDuration = mapEndTime - mapStartTime;

        if (transactionFoundByMap != null) {
            IO.println("Tempo de busca no map em nanossegundos: " + mapDuration);
        }
        IO.println("------------------------------------------------------------------------------");

    }
}