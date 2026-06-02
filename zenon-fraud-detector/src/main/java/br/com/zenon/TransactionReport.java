package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private record ReportTransaction(BigDecimal amount, boolean isFraud) {
    }

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {
     private static Statistics ZERO = new Statistics(0, 0, BigDecimal.ZERO);
     private Statistics addReportTransaction(ReportTransaction reportTransaction) {
         return new Statistics(
                 totalTransactions + 1,
                 totalFrauds + (reportTransaction.isFraud?  1 : 0),
                 totalAmount.add(reportTransaction.amount));
        }
     }

    public Statistics generateReport(String fileName) {
        Path path = Path.of(fileName);
        try (Stream<String> lines = Files.lines(path)){
             return lines
                    .skip(1)
                    .map(this::parseReportTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                     .reduce(Statistics.ZERO,
                             Statistics::addReportTransaction
                              ,(s1, s2) -> s1);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo " + fileName, ex);
        }
    }
    private Optional<ReportTransaction> parseReportTransaction(String line) {
        try {
            String[] chunks = line.split(",");

            if (chunks [2] == null ||  chunks [2] .trim().isEmpty()) throw new IllegalArgumentException("Amount is not null: " + chunks[2]);
            BigDecimal amount = new BigDecimal(chunks[2]);

            boolean isFraud = "1".equals(chunks[9]);

            return Optional.of(new ReportTransaction(amount,isFraud));
        } catch (Exception e) {
            System.err.println("Erro ao fazer o parse: " + line + "|" + e);
            return Optional.empty();
        }
    }
}
