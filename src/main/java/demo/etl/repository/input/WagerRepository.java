package demo.etl.repository.input;

import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface WagerRepository extends JpaRepository<Wager, String> {

    /**
     * Find wager summary by wager timestamp
     *
     * @param currentDate
     * @param pageable
     * @return
     */
    @Query("SELECT w.accountId, Date(w.wagerTimestamp), sum(w.wagerAmount) " +
            "FROM Wager w " +
            "WHERE Date(w.wagerTimestamp) = :currentDate " +
            "GROUP BY w.accountId")
    Page<Object> findWagerSummaries(Date currentDate, Pageable pageable);

    List<Wager> findByWagerTimestampBetween(LocalDateTime start, LocalDateTime end);
}
