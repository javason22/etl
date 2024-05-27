package demo.etl.core;

import demo.etl.core.extractor.SummaryDTOExtractor;
import demo.etl.core.loader.WagerSummaryLoader;
import demo.etl.core.processor.SummaryDTOToWagerSummaryEtlProcessor;
import demo.etl.core.transformer.SummaryDTOTransformer;
import demo.etl.dto.SummaryDTO;
import demo.etl.dto.req.EtlRequest;
import demo.etl.entity.output.WagerSummary;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SummaryDTOToWagerSummaryEtlProcessorTest {

    private SummaryDTOToWagerSummaryEtlProcessor summaryDTOToWagerSummaryEtlProcessor;

    @Mock
    private WagerSummaryLoader wagerSummaryLoader;

    @Mock
    private SummaryDTOExtractor summaryDTOExtractor;

    private List<SummaryDTO> testInputWagers; // mocking wagers for testing

    @Captor
    private ArgumentCaptor<List<WagerSummary>> wagerSummaryCaptor;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        summaryDTOToWagerSummaryEtlProcessor = new SummaryDTOToWagerSummaryEtlProcessor(summaryDTOExtractor, new SummaryDTOTransformer(), wagerSummaryLoader);
        testInputWagers = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        testInputWagers.add(SummaryDTO.builder()
                .accountId("00001")
                .totalWagerAmount(new BigDecimal("100.01"))
                .wagerDate(LocalDate.parse("2024-01-01", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00002")
                .totalWagerAmount(new BigDecimal("200.01"))
                .wagerDate(LocalDate.parse("2024-01-01", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00003")
                .totalWagerAmount(new BigDecimal("300.01"))
                .wagerDate(LocalDate.parse("2024-01-01", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00004")
                .totalWagerAmount(new BigDecimal("400.01"))
                .wagerDate(LocalDate.parse("2024-01-01", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00005")
                .totalWagerAmount(new BigDecimal("500.01"))
                .wagerDate(LocalDate.parse("2024-01-01", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00001")
                .totalWagerAmount(new BigDecimal("600.01"))
                .wagerDate(LocalDate.parse("2024-01-02", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00002")
                .totalWagerAmount(new BigDecimal("700.01"))
                .wagerDate(LocalDate.parse("2024-01-02", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00003")
                .totalWagerAmount(new BigDecimal("800.01"))
                .wagerDate(LocalDate.parse("2024-01-02", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00004")
                .totalWagerAmount(new BigDecimal("900.01"))
                .wagerDate(LocalDate.parse("2024-01-02", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00005")
                .totalWagerAmount(new BigDecimal("1000.01"))
                .wagerDate(LocalDate.parse("2024-01-02", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00001")
                .totalWagerAmount(new BigDecimal("1100.01"))
                .wagerDate(LocalDate.parse("2024-01-03", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00002")
                .totalWagerAmount(new BigDecimal("1200.01"))
                .wagerDate(LocalDate.parse("2024-01-03", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00003")
                .totalWagerAmount(new BigDecimal("1300.01"))
                .wagerDate(LocalDate.parse("2024-01-03", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00004")
                .totalWagerAmount(new BigDecimal("1400.01"))
                .wagerDate(LocalDate.parse("2024-01-03", formatter)).build());

        testInputWagers.add(SummaryDTO.builder()
                .accountId("00005")
                .totalWagerAmount(new BigDecimal("1500.01"))
                .wagerDate(LocalDate.parse("2024-01-03", formatter)).build());

    }

    @Test
    public void testProcessSingleBatch() {
        // Arrange
        EtlRequest request = new EtlRequest("2024-01-01", "2024-01-05", false);

        List<SummaryDTO> input = testInputWagers.subList(0, 15);
        when(summaryDTOExtractor.extract(request, 0, 20)).thenReturn(input);
        when(wagerSummaryLoader.load(any())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Act
        summaryDTOToWagerSummaryEtlProcessor.process(request, 20);

        // Assert
        verify(wagerSummaryLoader).load(wagerSummaryCaptor.capture());
        List<WagerSummary> capturedWagerSummaries = wagerSummaryCaptor.getValue();
        assertEquals(15, capturedWagerSummaries.size());
        // assert all the out objects
        assertEquals(WagerSummary.builder().accountId("00001")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("100.01")).
                build(), capturedWagerSummaries.get(0));
        assertEquals(WagerSummary.builder().accountId("00002")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("200.01")).
                build(), capturedWagerSummaries.get(1));
        assertEquals(WagerSummary.builder().accountId("00003")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("300.01")).
                build(), capturedWagerSummaries.get(2));
        assertEquals(WagerSummary.builder().accountId("00004")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("400.01")).
                build(), capturedWagerSummaries.get(3));
        assertEquals(WagerSummary.builder().accountId("00005")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("500.01")).
                build(), capturedWagerSummaries.get(4));
        assertEquals(WagerSummary.builder().accountId("00001")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("600.01")).
                build(), capturedWagerSummaries.get(5));
        assertEquals(WagerSummary.builder().accountId("00002")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("700.01")).
                build(), capturedWagerSummaries.get(6));
        assertEquals(WagerSummary.builder().accountId("00003")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("800.01")).
                build(), capturedWagerSummaries.get(7));
        assertEquals(WagerSummary.builder().accountId("00004")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("900.01")).
                build(), capturedWagerSummaries.get(8));
        assertEquals(WagerSummary.builder().accountId("00005")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("1000.01")).
                build(), capturedWagerSummaries.get(9));
        assertEquals(WagerSummary.builder().accountId("00001")
                .wagerDate(LocalDate.parse("2024-01-03"))
                .totalWagerAmount(new BigDecimal("1100.01")).
                build(), capturedWagerSummaries.get(10));
        assertEquals(WagerSummary.builder().accountId("00002")
                .wagerDate(LocalDate.parse("2024-01-03"))
                .totalWagerAmount(new BigDecimal("1200.01")).
                build(), capturedWagerSummaries.get(11));
        assertEquals(WagerSummary.builder().accountId("00003")
                .wagerDate(LocalDate.parse("2024-01-03"))
                .totalWagerAmount(new BigDecimal("1300.01")).
                build(), capturedWagerSummaries.get(12));
        assertEquals(WagerSummary.builder().accountId("00004")
                .wagerDate(LocalDate.parse("2024-01-03"))
                .totalWagerAmount(new BigDecimal("1400.01")).
                build(), capturedWagerSummaries.get(13));
        assertEquals(WagerSummary.builder().accountId("00005")
                .wagerDate(LocalDate.parse("2024-01-03"))
                .totalWagerAmount(new BigDecimal("1500.01")).
                build(), capturedWagerSummaries.get(14));
    }

    @Test
    public void testProcessMultipleBatch() {
        // Arrange
        EtlRequest request = new EtlRequest("2024-01-01", "2024-01-05", false);

        List<SummaryDTO> input1 = new ArrayList<>(testInputWagers.subList(0, 5));
        List<SummaryDTO> input2 = new ArrayList<>(testInputWagers.subList(5, 10));
        List<SummaryDTO> input3 = new ArrayList<>(testInputWagers.subList(10, 12));
        when(summaryDTOExtractor.extract(request, 0, 5)).thenReturn(input1);
        when(summaryDTOExtractor.extract(request, 1, 5)).thenReturn(input2);
        when(summaryDTOExtractor.extract(request, 2, 5)).thenReturn(input3);
        when(wagerSummaryLoader.load(any())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Act
        summaryDTOToWagerSummaryEtlProcessor.process(request, 5);

        // Assert
        verify(wagerSummaryLoader, times(3)).load(wagerSummaryCaptor.capture());
        List<List<WagerSummary>> capturedWagerSummaries = wagerSummaryCaptor.getAllValues();
        assertEquals(3, capturedWagerSummaries.size());
        assertEquals(5, capturedWagerSummaries.get(0).size());
        assertEquals(5, capturedWagerSummaries.get(1).size());
        assertEquals(2, capturedWagerSummaries.get(2).size());

        // assert all the out objects
        assertEquals(WagerSummary.builder().accountId("00001")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("100.01")).
                build(), capturedWagerSummaries.get(0).get(0));
        assertEquals(WagerSummary.builder().accountId("00002")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("200.01")).
                build(), capturedWagerSummaries.get(0).get(1));
        assertEquals(WagerSummary.builder().accountId("00003")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("300.01")).
                build(), capturedWagerSummaries.get(0).get(2));
        assertEquals(WagerSummary.builder().accountId("00004")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("400.01")).
                build(), capturedWagerSummaries.get(0).get(3));
        assertEquals(WagerSummary.builder().accountId("00005")
                .wagerDate(LocalDate.parse("2024-01-01"))
                .totalWagerAmount(new BigDecimal("500.01")).
                build(), capturedWagerSummaries.get(0).get(4));
        assertEquals(WagerSummary.builder().accountId("00001")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("600.01")).
                build(), capturedWagerSummaries.get(1).get(0));
        assertEquals(WagerSummary.builder().accountId("00002")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("700.01")).
                build(), capturedWagerSummaries.get(1).get(1));
        assertEquals(WagerSummary.builder().accountId("00003")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("800.01")).
                build(), capturedWagerSummaries.get(1).get(2));
        assertEquals(WagerSummary.builder().accountId("00004")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("900.01")).
                build(), capturedWagerSummaries.get(1).get(3));
        assertEquals(WagerSummary.builder().accountId("00005")
                .wagerDate(LocalDate.parse("2024-01-02"))
                .totalWagerAmount(new BigDecimal("1000.01")).
                build(), capturedWagerSummaries.get(1).get(4));
        assertEquals(WagerSummary.builder().accountId("00001")
                .wagerDate(LocalDate.parse("2024-01-03"))
                .totalWagerAmount(new BigDecimal("1100.01")).
                build(), capturedWagerSummaries.get(2).get(0));
        assertEquals(WagerSummary.builder().accountId("00002")
                .wagerDate(LocalDate.parse("2024-01-03"))
                .totalWagerAmount(new BigDecimal("1200.01")).
                build(), capturedWagerSummaries.get(2).get(1));

    }
}
