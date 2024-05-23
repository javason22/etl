package demo.etl.repository.output;

import demo.etl.entity.output.WagerSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WagerSummaryRepository extends JpaRepository<WagerSummary, Long>{

    void deleteByWagerDate(LocalDate wagerTimestamp);
}
