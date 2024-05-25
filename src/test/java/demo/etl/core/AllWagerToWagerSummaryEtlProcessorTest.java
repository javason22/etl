package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AllWagerToWagerSummaryEtlProcessorTest {

    //@Mock
    @Mock
    private WagerExtractor allWagerExtractor;
    //@Mock
    @Mock
    private WagerSummaryLoader wagerSummaryLoader;

    private WagerToWagerSummaryEtlProcessor allWagerToWagerSummaryEtlProcessor;

    @Captor
    private ArgumentCaptor<List<WagerSummary>> wagerSummaryCaptor;

    @BeforeEach
    public void setUp() {
        allWagerToWagerSummaryEtlProcessor = new WagerToWagerSummaryEtlProcessor(allWagerExtractor, new WagerSummaryTransformer(), wagerSummaryLoader);
    }
    @Test
    @SneakyThrows
    public void testProcess() {
        // Arrange
        Sort sort = Sort.by(Sort.Order.asc("accountId"), Sort.Order.asc("wagerTimestamp"));
        Pageable firstPage = PageRequest.of(0, 5, sort); // initial page
        // create 12 mock wagers data
        Wager wager1 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("100.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 00:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager2 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("200.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 01:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager3 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("100.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 01:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager4 = Wager.builder()
                .accountId("00001")
                .wagerAmount(new BigDecimal("0.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 01:00:01", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager5 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("300.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 01:10:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager6 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("400.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 23:59:59", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager7 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("300.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 23:59:59", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager8 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("200.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 06:30:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager9 = Wager.builder()
                .accountId("00002")
                .wagerAmount(new BigDecimal("500.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-02 04:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager10 = Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("500.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 12:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager11 = Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("600.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-01 11:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        Wager wager12 = Wager.builder()
                .accountId("00003")
                .wagerAmount(new BigDecimal("600.01"))
                .wagerTimestamp(DateUtils.parseDate("2024-01-03 08:00:00", "yyyy-MM-dd hh:mm:ss")).build();

        // custom matchers to verify the pageable of 3 pages extraction
        ArgumentMatcher<Pageable> firstPageMatcher = pageable -> pageable.getPageNumber() == 0 && pageable.getPageSize() == 5;
        ArgumentMatcher<Pageable> secondPageMatcher = pageable -> pageable.getPageNumber() == 1 && pageable.getPageSize() == 5;
        ArgumentMatcher<Pageable> thirdPageMatcher = pageable -> pageable.getPageNumber() == 2 && pageable.getPageSize() == 5;

        List<Wager> wagers1Loop = new ArrayList<>();
        wagers1Loop.addAll(Arrays.asList(wager1, wager2, wager3, wager4, wager5));
        List<Wager> wagers2Loop = new ArrayList<>();
        wagers2Loop.addAll(Arrays.asList(wager6, wager7, wager8, wager9, wager10));
        List<Wager> wagers3Loop = new ArrayList<>();
        wagers3Loop.addAll(Arrays.asList(wager11, wager12));

        doReturn(wagers1Loop).when(allWagerExtractor).extract(argThat(firstPageMatcher));
        doReturn(wagers2Loop).when(allWagerExtractor).extract(argThat(secondPageMatcher));
        doReturn(wagers3Loop).when(allWagerExtractor).extract(argThat(thirdPageMatcher));

        //doReturn(CompletableFuture.completedFuture(new ArrayList<>())).when(wagerSummaryLoader).load(wagerSummaryCaptor.capture());
        when(wagerSummaryLoader.load(any())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        /*List<List<WagerSummary>> capturedWagerSummaries = new ArrayList<>();
        doAnswer(invocation -> {
            List<WagerSummary> arg = invocation.getArgument(0);
            // Now arg contains the list passed to load method
            // You can add your assertions here
            capturedWagerSummaries.add(arg);
            return CompletableFuture.completedFuture(new ArrayList<>());
        }).when(wagerSummaryLoader).load(any());*/

        // Act
        allWagerToWagerSummaryEtlProcessor.process(firstPage);

        // Assert
        verify(wagerSummaryLoader, times(4)).load(wagerSummaryCaptor.capture());

        List<List<WagerSummary>> capturedWagerSummaries = wagerSummaryCaptor.getAllValues();
        assertEquals(2, capturedWagerSummaries.get(0).size());
        assertEquals(2, capturedWagerSummaries.get(1).size());
        assertEquals(1, capturedWagerSummaries.get(2).size());
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
                        assertEquals(new BigDecimal("600.01"), wagerSummary.getTotalWagerAmount());
                    }
                }
            }
        }

        verify(allWagerExtractor).extract(argThat(firstPageMatcher));
        verify(allWagerExtractor).extract(argThat(secondPageMatcher));
        verify(allWagerExtractor).extract(argThat(thirdPageMatcher));

    }
}
