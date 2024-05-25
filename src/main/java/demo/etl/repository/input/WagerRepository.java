package demo.etl.repository.input;

import demo.etl.dto.SummaryDTO;
import demo.etl.entity.input.Wager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface WagerRepository extends JpaRepository<Wager, String> {

    /**
     * Find wager summary grouped by account id and wager date
     *
     * @param date     the date to filter
     * @return the list of wager summaries
     */
    @Query("SELECT new demo.etl.dto.SummaryDTO(w.accountId, Date(w.wagerTimestamp), sum(w.wagerAmount)) " +
            "FROM Wager w " +
            "WHERE Date(w.wagerTimestamp) = :date " +
            "GROUP BY w.accountId " +
            "ORDER BY w.accountId")
    List<SummaryDTO> findWagerSummaries(LocalDate date);

    /**
     * Find wager summary grouped by account id and wager date
     *
     * @return the list of wager summaries
     */
    @Query("SELECT new demo.etl.dto.SummaryDTO(w.accountId, Date(w.wagerTimestamp), sum(w.wagerAmount)) " +
            "FROM Wager w " +
            "GROUP BY w.accountId, Date(w.wagerTimestamp) " +
            "ORDER BY w.accountId, Date(w.wagerTimestamp) " +
            "LIMIT :limit OFFSET :offset")
    List<SummaryDTO> findAllWagerSummaries(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * Find wagers by wager timestamp between start and end
     *
     * @param start the start timestamp
     * @param end   the end timestamp
     * @return the list of wagers
     */
    List<Wager> findByWagerTimestampBetween(LocalDateTime start, LocalDateTime end);
}
