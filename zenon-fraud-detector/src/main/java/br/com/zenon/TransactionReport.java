package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

public class TransactionReport {

    public void printSummary(String fileName) {
        Path path = Path.of(fileName);

        long totalTransactions = 0;
        long totalFrauds = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        try (var lines = Files.lines(path)) {
            var iterator = lines
                    .skip(1)
                    .iterator();

            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] chunks = line.split(",");

                totalTransactions++;

                BigDecimal amount = new BigDecimal(chunks[2]);
                totalAmount = totalAmount.add(amount);

                boolean isFraud = "1".equals(chunks[9]);
                if (isFraud) {
                    totalFrauds++;
                }
            }

            IO.println("Total de transações no arquivo: " + totalTransactions);
            IO.println("Total de fraudes no arquivo: " + totalFrauds);
            IO.println("Valor total transacionado: " + totalAmount);
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao ler o arquivo " + fileName, ex);
        }
    }
}