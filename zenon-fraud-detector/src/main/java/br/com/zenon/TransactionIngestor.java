package br.com.zenon;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TransactionIngestor {

    public List<Transaction> read(String filename) {
        Path path = Path.of(filename);
        // Files.lines abre um Stream que não carrega o arquivo todo na RAM
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1) // Pula o cabeçalho
                    .limit(1000)
                    .map(this::parseTransaction)
                    .toList();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo: " + filename, ex);
        }
    }

    private Transaction parseTransaction(String line) {
        String[] chunks = line.split(",");

        return null;
    }
}
