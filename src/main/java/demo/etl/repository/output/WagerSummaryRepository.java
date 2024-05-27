package demo.etl.repository.output;

import demo.etl.entity.output.WagerSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WagerSummaryRepository extends JpaRepository<WagerSummary, String>{

    List<WagerSummary> findByWagerDateBetween(LocalDate start, LocalDate end);
}
