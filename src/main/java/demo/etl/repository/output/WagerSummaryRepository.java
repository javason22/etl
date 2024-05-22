package demo.etl.repository.output;

import demo.etl.entity.WagerSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WagerSummaryRepository extends JpaRepository<WagerSummary, Long>{
}
