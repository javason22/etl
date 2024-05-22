package demo.etl.repository.input;

import demo.etl.entity.Wager;
import demo.etl.entity.WagerSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WagerRepository extends JpaRepository<Wager, Long> {

    /**
     * Find wager summary by wager timestamp
     *
     * @param currentDate
     * @param pageable
     * @return
     */
    @Query("SELECT new demo.etl.entity.WagerSummary(w.accountId, Date(w.wagerTimestamp), sum(w.wagerAmount)) " +
            "FROM wager w " +
            "WHERE Date(w.wagerTimestamp) = :currentDate " +
            "GROUP BY w.accountId")
    Page<WagerSummary> findWagerSummaries(Date currentDate, Pageable pageable);
}
