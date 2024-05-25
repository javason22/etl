package demo.etl.core;

import demo.etl.core.extractor.WagerExtractor;
import demo.etl.core.loader.WagerSummaryLoader;
import demo.etl.core.processor.WagerToWagerSummaryEtlProcessor;
import demo.etl.core.transformer.WagerSummaryTransformer;
import demo.etl.dto.req.EtlRequest;
import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class WagerToWagerSummaryEtlProcessorTest {

    @Mock
    private WagerExtractor allWagerExtractor;

    @Mock
    private WagerSummaryLoader wagerSummaryLoader;

    private WagerToWagerSummaryEtlProcessor allWagerToWagerSummaryEtlProcessor;

    @Captor
    private ArgumentCaptor<List<WagerSummary>> wagerSummaryCaptor;

    private List<Wager> testInputWagers; // mocking wagers for testing

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        allWagerToWagerSummaryEtlProcessor = new WagerToWagerSummaryEtlProcessor(allWagerExtractor, new WagerSummaryTransformer(), wagerSummaryLoader);
        testInputWagers = new ArrayList<>();
        testInputWagers.add(Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("100.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 00:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("200.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 01:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("100.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 01:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("0.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 01:00:01", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("300.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 01:10:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("400.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 23:59:59", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("300.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 23:59:59", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("200.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 06:30:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("500.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 04:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("500.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 12:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("600.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 11:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("600.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-03 08:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("100.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-03 12:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("200.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-04 11:00:00", "yyyy-MM-dd hh:mm:ss")).build());

        testInputWagers.add(Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("300.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-04 08:00:00", "yyyy-MM-dd hh:mm:ss")).build());

    }

    @Test
    public void testProcessSingleBatch() {
        // Arrange
        EtlRequest request = new EtlRequest("2024-01-01", "2024-01-05");

        // custom matchers to verify the pageable of 3 pages extraction
        //ArgumentMatcher<Pageable> firstPageMatcher = pageable -> pageable.getPageNumber() == 0 && pageable.getPageSize() == 5;
        //ArgumentMatcher<Pageable> secondPageMatcher = pageable -> pageable.getPageNumber() == 1 && pageable.getPageSize() == 5;
        //ArgumentMatcher<Pageable> thirdPageMatcher = pageable -> pageable.getPageNumber() == 2 && pageable.getPageSize() == 5;

        List<Wager> wagers = testInputWagers.subList(0, 15);

        doReturn(wagers).when(allWagerExtractor).extract(request, 0, 20);

        when(wagerSummaryLoader.load(any())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Act
        allWagerToWagerSummaryEtlProcessor.process(request, 20);

        // Assert
        verify(wagerSummaryLoader).load(wagerSummaryCaptor.capture());

        List<WagerSummary> capturedWagerSummaries = wagerSummaryCaptor.getValue();
        assertEquals(7, capturedWagerSummaries.size());

        for(WagerSummary wagerSummary : capturedWagerSummaries) {
            log.debug("WagerSummary: {}", wagerSummary);
            if(wagerSummary.getAccountId().equals("00001")) {
                if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                    assertEquals(new BigDecimal("300.02"), wagerSummary.getTotalWagerAmount());
                } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))) {
                    assertEquals(new BigDecimal("100.02"), wagerSummary.getTotalWagerAmount());
                }
            } else if(wagerSummary.getAccountId().equals("00002")) {
                if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                    assertEquals(new BigDecimal("700.02"), wagerSummary.getTotalWagerAmount());
                } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))) {
                    assertEquals(new BigDecimal("1000.03"), wagerSummary.getTotalWagerAmount());
                }
            } else if(wagerSummary.getAccountId().equals("00003")) {
                if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                    assertEquals(new BigDecimal("1100.02"), wagerSummary.getTotalWagerAmount());
                } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 3))) {
                    assertEquals(new BigDecimal("700.02"), wagerSummary.getTotalWagerAmount());
                } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 4))) {
                    assertEquals(new BigDecimal("500.02"), wagerSummary.getTotalWagerAmount());
                }
            }

        }

        verify(allWagerExtractor).extract(request, 0, 20);

    }
    @Test
    public void testProcessNormalFinalBatch() {
        // Arrange
        EtlRequest request = new EtlRequest("2024-01-01", "2024-01-05");

        // custom matchers to verify the pageable of 3 pages extraction
        //ArgumentMatcher<Pageable> firstPageMatcher = pageable -> pageable.getPageNumber() == 0 && pageable.getPageSize() == 5;
        //ArgumentMatcher<Pageable> secondPageMatcher = pageable -> pageable.getPageNumber() == 1 && pageable.getPageSize() == 5;
        //ArgumentMatcher<Pageable> thirdPageMatcher = pageable -> pageable.getPageNumber() == 2 && pageable.getPageSize() == 5;

        List<Wager> wagers1Loop = testInputWagers.subList(0, 5);
        List<Wager> wagers2Loop = testInputWagers.subList(5, 10);
        List<Wager> wagers3Loop = testInputWagers.subList(10, 12);

        doReturn(wagers1Loop).when(allWagerExtractor).extract(request, 0, 5);
        doReturn(wagers2Loop).when(allWagerExtractor).extract(request, 1, 5);
        doReturn(wagers3Loop).when(allWagerExtractor).extract(request, 2, 5);

        when(wagerSummaryLoader.load(any())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Act
        allWagerToWagerSummaryEtlProcessor.process(request, 5);

        // Assert
        verify(wagerSummaryLoader, times(3)).load(wagerSummaryCaptor.capture());

        List<List<WagerSummary>> capturedWagerSummaries = wagerSummaryCaptor.getAllValues();
        assertEquals(2, capturedWagerSummaries.get(0).size());
        assertEquals(2, capturedWagerSummaries.get(1).size());
        assertEquals(2, capturedWagerSummaries.get(2).size());

        for(List<WagerSummary> wagerSummaries : capturedWagerSummaries) {
            for(WagerSummary wagerSummary : wagerSummaries) {
                log.debug("WagerSummary: {}", wagerSummary);
                if(wagerSummary.getAccountId().equals("00001")) {
                    if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                        assertEquals(new BigDecimal("300.02"), wagerSummary.getTotalWagerAmount());
                    } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))) {
                        assertEquals(new BigDecimal("100.02"), wagerSummary.getTotalWagerAmount());
                    }
                } else if(wagerSummary.getAccountId().equals("00002")) {
                    if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                        assertEquals(new BigDecimal("700.02"), wagerSummary.getTotalWagerAmount());
                    } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))) {
                        assertEquals(new BigDecimal("1000.03"), wagerSummary.getTotalWagerAmount());
                    }
                } else if(wagerSummary.getAccountId().equals("00003")) {
                    if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                        assertEquals(new BigDecimal("1100.02"), wagerSummary.getTotalWagerAmount());
                    } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 3))) {
                        assertEquals(new BigDecimal("600.01"), wagerSummary.getTotalWagerAmount());
                    }
                }
            }
        }

        //verify(allWagerExtractor).extract(argThat(firstPageMatcher));
        //verify(allWagerExtractor).extract(argThat(secondPageMatcher));
        //verify(allWagerExtractor).extract(argThat(thirdPageMatcher));

        verify(allWagerExtractor).extract(request, 0, 5);
        verify(allWagerExtractor).extract(request, 1, 5);
        verify(allWagerExtractor).extract(request, 2, 5);

    }

    @Test
    public void testProcessFullFinalBatch() {
        // Arrange
        EtlRequest request = new EtlRequest("2024-01-01", "2024-01-05");

        // custom matchers to verify the pageable of 3 pages extraction
        //ArgumentMatcher<Pageable> firstPageMatcher = pageable -> pageable.getPageNumber() == 0 && pageable.getPageSize() == 5;
        //ArgumentMatcher<Pageable> secondPageMatcher = pageable -> pageable.getPageNumber() == 1 && pageable.getPageSize() == 5;
        //ArgumentMatcher<Pageable> thirdPageMatcher = pageable -> pageable.getPageNumber() == 2 && pageable.getPageSize() == 5;

        List<Wager> wagers1Loop = testInputWagers.subList(0, 5);
        List<Wager> wagers2Loop = testInputWagers.subList(5, 10);
        List<Wager> wagers3Loop = testInputWagers.subList(10, 15);

        doReturn(wagers1Loop).when(allWagerExtractor).extract(request, 0, 5);
        doReturn(wagers2Loop).when(allWagerExtractor).extract(request, 1, 5);
        doReturn(wagers3Loop).when(allWagerExtractor).extract(request, 2, 5);

        when(wagerSummaryLoader.load(any())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Act
        allWagerToWagerSummaryEtlProcessor.process(request, 5);

        // Assert
        verify(wagerSummaryLoader, times(4)).load(wagerSummaryCaptor.capture());

        List<List<WagerSummary>> capturedWagerSummaries = wagerSummaryCaptor.getAllValues();
        assertEquals(2, capturedWagerSummaries.get(0).size());
        assertEquals(2, capturedWagerSummaries.get(1).size());
        assertEquals(2, capturedWagerSummaries.get(2).size());
        assertEquals(1, capturedWagerSummaries.get(3).size());

        for(List<WagerSummary> wagerSummaries : capturedWagerSummaries) {
            for(WagerSummary wagerSummary : wagerSummaries) {
                log.debug("WagerSummary: {}", wagerSummary);
                if(wagerSummary.getAccountId().equals("00001")) {
                    if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                        assertEquals(new BigDecimal("300.02"), wagerSummary.getTotalWagerAmount());
                    } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))) {
                        assertEquals(new BigDecimal("100.02"), wagerSummary.getTotalWagerAmount());
                    }
                } else if(wagerSummary.getAccountId().equals("00002")) {
                    if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                        assertEquals(new BigDecimal("700.02"), wagerSummary.getTotalWagerAmount());
                    } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 2))) {
                        assertEquals(new BigDecimal("1000.03"), wagerSummary.getTotalWagerAmount());
                    }
                } else if(wagerSummary.getAccountId().equals("00003")) {
                    if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 1))) {
                        assertEquals(new BigDecimal("1100.02"), wagerSummary.getTotalWagerAmount());
                    } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 3))) {
                        assertEquals(new BigDecimal("700.02"), wagerSummary.getTotalWagerAmount());
                    } else if(wagerSummary.getWagerDate().isEqual(LocalDate.of(2024, 1, 4))) {
                        assertEquals(new BigDecimal("500.02"), wagerSummary.getTotalWagerAmount());
                    }
                }
            }
        }

        //verify(allWagerExtractor).extract(argThat(firstPageMatcher));
        //verify(allWagerExtractor).extract(argThat(secondPageMatcher));
        //verify(allWagerExtractor).extract(argThat(thirdPageMatcher));

        verify(allWagerExtractor).extract(request, 0, 5);
        verify(allWagerExtractor).extract(request, 1, 5);
        verify(allWagerExtractor).extract(request, 2, 5);

    }
}
