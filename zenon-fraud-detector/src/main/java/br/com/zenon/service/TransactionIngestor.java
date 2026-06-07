package br.com.zenon.service;

import br.com.zenon.domain.Transaction;
import br.com.zenon.domain.TransactionCustumer;
import br.com.zenon.domain.TransactionType;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    public static final int FRAUD_LIMIT = 10_000;

    public List<Transaction> read(String filename) {
        Path path = Path.of(filename);
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(FRAUD_LIMIT)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo: " + filename, ex);
        }
    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] chunks = line.split(",");

            if (chunks.length < 11) {
                throw new IllegalArgumentException("Linha inválida: número insuficiente de colunas");
            }

            TransactionType type = TransactionType.valueOf(chunks[1]);

            if (chunks[2] == null || chunks[2].trim().isEmpty()) {
                throw new IllegalArgumentException("O valor de amount não pode ser nulo ou vazio");
            }

            BigDecimal amount = new BigDecimal(chunks[2]);
            TransactionCustumer origin = new TransactionCustumer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
            TransactionCustumer recipient = new TransactionCustumer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));

            int step = Integer.parseInt(chunks[0]);

            boolean isFraud = "1".equals(chunks[9]);
            boolean isFlaggedFraud = "1".equals(chunks[10]);

            return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));
        } catch (Exception ex) {
            System.err.println("Erro ao fazer parse: " + line + " - " + ex.getMessage());
        }
        return Optional.empty();
    }
}
