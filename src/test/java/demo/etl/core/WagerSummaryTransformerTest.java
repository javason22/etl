package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WagerSummaryTransformerTest {

    private WagerSummaryTransformer wagerSummaryTransformer;

    @BeforeEach
    public void setup() {
        wagerSummaryTransformer = new WagerSummaryTransformer();
    }

    /**
     * Test the transformation of all wagers to wager summaries
     */
    @SneakyThrows
    @Test
    public void testTransformAllWagers() {
        // Arrange
        Wager wager1 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("100.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 00:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager2 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("200.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 00:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager3 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("300.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 01:10:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager4 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("400.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 23:59:59", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager5 = Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("500.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 12:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager6 = Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("600.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 11:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager7 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("100.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 01:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager8 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("0.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 01:00:01", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager9 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("300.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 23:59:59", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager10 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("400.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 06:30:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager11 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("500.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 04:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager12 = Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("600.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-03 08:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        List<Wager> allWagers = Arrays.asList(wager1, wager2, wager3, wager4, wager5, wager6, wager7, wager8, wager9, wager10, wager11, wager12);

        // Act
        List<WagerSummary> result = wagerSummaryTransformer.transform(allWagers);

        // Assert
        assertEquals(6, result.size());
        assertEquals(new BigDecimal("300.02"), result.stream().filter(wagerSummary -> wagerSummary.getAccountId().equals("00001") && wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))).findFirst().get().getTotalWagerAmount());
        assertEquals(new BigDecimal("700.02"), result.stream().filter(wagerSummary -> wagerSummary.getAccountId().equals("00002") && wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))).findFirst().get().getTotalWagerAmount());
        assertEquals(new BigDecimal("1100.02"), result.stream().filter(wagerSummary -> wagerSummary.getAccountId().equals("00003") && wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))).findFirst().get().getTotalWagerAmount());
        assertEquals(new BigDecimal("100.02"), result.stream().filter(wagerSummary -> wagerSummary.getAccountId().equals("00001") && wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))).findFirst().get().getTotalWagerAmount());
        assertEquals(new BigDecimal("1200.03"), result.stream().filter(wagerSummary -> wagerSummary.getAccountId().equals("00002") && wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))).findFirst().get().getTotalWagerAmount());
        assertEquals(new BigDecimal("600.01"), result.stream().filter(wagerSummary -> wagerSummary.getAccountId().equals("00003") && wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 3))).findFirst().get().getTotalWagerAmount());

    }
}
