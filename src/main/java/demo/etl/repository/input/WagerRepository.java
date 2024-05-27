package demo.etl.repository.input;

import demo.etl.dto.SummaryDTO;
import demo.etl.entity.input.Wager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface WagerRepository extends JpaRepository<Wager, String> {


    /**
     * Find wager summary grouped by account id and wager date
     *
     * @return the list of wager summaries
     */
    @Query("SELECT new demo.etl.dto.SummaryDTO(w.accountId, Date(w.wagerTimestamp), sum(w.wagerAmount)) " +
            "FROM Wager w " +
            "WHERE w.wagerTimestamp BETWEEN :start AND :end " +
            "GROUP BY w.accountId, Date(w.wagerTimestamp) " +
            "ORDER BY w.accountId, Date(w.wagerTimestamp)")
    Page<SummaryDTO> findAllWagerSummaries(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    /**
     * Find wager summary grouped by account id and wager date
     *
     * @return the list of wager summaries
     */
    @Query("SELECT new demo.etl.dto.SummaryDTO(w.accountId, Date(w.wagerTimestamp), sum(w.wagerAmount)) " +
            "FROM Wager w " +
            "GROUP BY w.accountId, Date(w.wagerTimestamp) " +
            "ORDER BY w.accountId, Date(w.wagerTimestamp)")
    Page<SummaryDTO> findAllWagerSummaries(Pageable pageable);

    Page<Wager> findByWagerTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

}
