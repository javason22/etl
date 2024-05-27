package demo.etl.service;

import demo.etl.core.processor.SummaryDTOToWagerSummaryEtlProcessor;
import demo.etl.core.processor.WagerToWagerSummaryEtlProcessor;
import demo.etl.dto.req.EtlRequest;
import demo.etl.repository.output.WagerSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EtlServiceTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @Mock
    private WagerSummaryRepository wagerSummaryRepository;

    @Mock
    private WagerToWagerSummaryEtlProcessor wagerToWagerSummaryEtlProcessor;

    @Mock
    private SummaryDTOToWagerSummaryEtlProcessor summaryDTOToWagerSummaryEtlProcessor;

    @InjectMocks
    private EtlService etlService;

    private static final String WAGER_SUMMARY_LOCK = "wager-summary-lock";

    @BeforeEach
    public void setUp() {
        when(redissonClient.getLock(WAGER_SUMMARY_LOCK)).thenReturn(rLock);
    }

    @Test
    public void testTransformWagersToWagerSummaries() {
        // Arrange
        EtlRequest request = new EtlRequest("2023-01-01", "2023-01-31", false);

        doNothing().when(wagerSummaryRepository).deleteAll(any());
        when(wagerToWagerSummaryEtlProcessor.process(eq(request), anyInt())).thenReturn(new ArrayList<>());

        // Act
        etlService.transformWagersToWagerSummaries(request);

        // Assert
        verify(rLock, times(1)).lock();
        verify(rLock, times(1)).unlock();
        verify(wagerSummaryRepository, times(1)).deleteAll(any());
        verify(wagerToWagerSummaryEtlProcessor, times(1)).process(eq(request), anyInt());
    }

    @Test
    public void testTransformWagersToWagerSummariesNullRequest() {
        // Arrange
        EtlRequest request = null;

        doNothing().when(wagerSummaryRepository).deleteAll();
        when(wagerToWagerSummaryEtlProcessor.process(eq(request), anyInt())).thenReturn(new ArrayList<>());

        // Act
        etlService.transformWagersToWagerSummaries(request);

        // Assert
        verify(rLock, times(1)).lock();
        verify(rLock, times(1)).unlock();
        verify(wagerSummaryRepository, times(1)).deleteAll();
        verify(wagerToWagerSummaryEtlProcessor, times(1)).process(eq(request), anyInt());
    }

    @Test
    public void testTransformSummaryDTOToWagerSummaries() {
        // Arrange
        EtlRequest request = new EtlRequest("2023-01-01", "2023-01-31", false);

        doNothing().when(wagerSummaryRepository).deleteAll(any(List.class));
        //when(wagerToWagerSummaryEtlProcessor.process(eq(request), anyInt())).thenReturn(new ArrayList<>());

        // Act
        etlService.transformSummaryDTOToWagerSummaries(request);

        // Assert
        verify(rLock, times(1)).lock();
        verify(rLock, times(1)).unlock();
        verify(wagerSummaryRepository, times(1)).deleteAll(any(List.class));
        verify(summaryDTOToWagerSummaryEtlProcessor, times(1)).process(eq(request), anyInt());
    }

    @Test
    public void testTransformSummaryDTOToWagerSummariesNullRequest() {
        // Arrange
        EtlRequest request = null;

        doNothing().when(wagerSummaryRepository).deleteAll();
        //doReturn(new ArrayList<CompletableFuture<List<WagerSummary>>>()).when(wagerToWagerSummaryEtlProcessor).process(nullable(EtlRequest.class), anyInt());

        // Act
        etlService.transformSummaryDTOToWagerSummaries(request);

        // Assert
        verify(rLock, times(1)).lock();
        verify(rLock, times(1)).unlock();
        verify(wagerSummaryRepository, times(1)).deleteAll();
        verify(summaryDTOToWagerSummaryEtlProcessor).process(nullable(EtlRequest.class), anyInt());
    }

    @Test
    public void testConcurrentTransformWagersToWagerSummaries() throws InterruptedException, ExecutionException {
        // Arrange
        EtlRequest request = new EtlRequest("2023-01-01", "2023-01-31", false);

        doNothing().when(wagerSummaryRepository).deleteAll(any(List.class));
        when(wagerToWagerSummaryEtlProcessor.process(eq(request), anyInt())).thenReturn(new ArrayList<>());

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        // Act
        Callable<Void> task = () -> {
            try {
                latch.countDown();
                latch.await();
                etlService.transformWagersToWagerSummaries(request);
            } finally {
                return null;
            }
        };

        Future<Void> future1 = executorService.submit(task);
        Future<Void> future2 = executorService.submit(task);
        Future<Void> future3 = executorService.submit(task);

        future1.get();
        future2.get();
        future3.get();

        // Assert
        verify(rLock, times(3)).lock();
        verify(rLock, times(3)).unlock();
        verify(wagerSummaryRepository, times(3)).deleteAll(any(List.class));
        verify(wagerToWagerSummaryEtlProcessor, times(3)).process(eq(request), anyInt());

        executorService.shutdown();
    }

    @Test
    public void testConcurrentTransformSummaryDTOToWagerSummaries() throws InterruptedException, ExecutionException {
        // Arrange
        EtlRequest request = new EtlRequest("2023-01-01", "2023-01-31", false);

        doNothing().when(wagerSummaryRepository).deleteAll(any(List.class));
        //doNothing().when(summaryDTOToWagerSummaryEtlProcessor).process(eq(request), anyInt());
        //when(wagerToWagerSummaryEtlProcessor.process(eq(request), anyInt())).thenReturn(new ArrayList<>());

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        // Act
        Callable<Void> task = () -> {
            try {
                latch.countDown();
                latch.await();
                etlService.transformSummaryDTOToWagerSummaries(request);
            } finally {
                return null;
            }
        };

        Future<Void> future1 = executorService.submit(task);
        Future<Void> future2 = executorService.submit(task);
        Future<Void> future3 = executorService.submit(task);

        future1.get();
        future2.get();
        future3.get();

        // Assert
        verify(rLock, times(3)).lock();
        verify(rLock, times(3)).unlock();
        verify(wagerSummaryRepository, times(3)).deleteAll(any(List.class));
        verify(summaryDTOToWagerSummaryEtlProcessor, times(3)).process(eq(request), anyInt());

        executorService.shutdown();
    }
}
