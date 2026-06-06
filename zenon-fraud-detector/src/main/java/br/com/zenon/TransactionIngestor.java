package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class TransactionIngestor {

    public List<Transaction> read(String fileName) {
        Path path = Path.of(fileName);
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .limit(1_000_000)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo " + fileName, ex);
        }
    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] chunks = line.split(",");

            int step = Integer.parseInt(chunks[0]);
            TransactionType type = TransactionType.valueOf(chunks[1]);

            if (chunks [2] == null ||  chunks [2] .trim().isEmpty()) throw new IllegalArgumentException("Amount is not null: " + chunks[2]);
            BigDecimal amount = new BigDecimal(chunks[2]);

            if (chunks[3] == null || chunks[3].trim().isEmpty()) throw new IllegalArgumentException("Origin is not null: ");
            var origin = new TransactionCustumer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));

            if (chunks[6] == null || chunks[6].trim().isEmpty()) throw new IllegalArgumentException("Recipient not is null: ");
            var recipient = new TransactionCustumer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));

            boolean isFraud = "1".equals(chunks[9]);
            boolean isFlaggedFraud = "1".equals(chunks[10]);

            return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));
        } catch (Exception e) {
            System.err.println("Erro ao fazer o parse: " + line + "|" + e);
            return Optional.empty();
        }
    }
}
