package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.repository.input.WagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AllWagerExtractorTest {

    private WagerRepository wagerRepository;
    private AllWagerExtractor allWagerExtractor;

    @BeforeEach
    public void setUp() {
        wagerRepository = Mockito.mock(WagerRepository.class);
        allWagerExtractor = new AllWagerExtractor(wagerRepository);
    }

    @Test
    public void testExtract() {
        // Arrange
        Wager wager1 = new Wager();
        Wager wager2 = new Wager();
        List<Wager> wagers = Arrays.asList(wager1, wager2);
        Page<Wager> page = new PageImpl<>(wagers);
        Pageable pageable = PageRequest.of(0, 2);
        when(wagerRepository.findAll(pageable)).thenReturn(page);

        // Act
        List<Wager> result = allWagerExtractor.extract(pageable);

        // Assert
        assertEquals(wagers, result);
        Mockito.verify(wagerRepository).findAll(pageable);
    }
}
