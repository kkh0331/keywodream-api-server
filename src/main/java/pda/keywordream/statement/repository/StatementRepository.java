package pda.keywordream.statement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.statement.entity.Statement;

import java.util.Optional;

public interface StatementRepository extends JpaRepository<Statement, Long> {

    Optional<Statement> findByStockCodeAndYear(String stockCode, String year);

}
