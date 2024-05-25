package demo.etl.repository.output;

import demo.etl.entity.output.WagerSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WagerSummaryRepository extends JpaRepository<WagerSummary, String>{

    @Query("DELETE FROM WagerSummary ws WHERE ws.wagerDate BETWEEN :start AND :end")
    void deleteByWagerDateBetween(LocalDate start, LocalDate end);
}
