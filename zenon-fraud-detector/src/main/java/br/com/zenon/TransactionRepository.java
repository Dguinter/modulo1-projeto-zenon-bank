package br.com.zenon;

import java.sql.SQLException;
import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByOriginName(String originName) throws SQLException;
}
