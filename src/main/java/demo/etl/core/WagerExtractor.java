package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class WagerExtractor implements Extractor<Wager, LocalDate>{

    private final WagerRepository wagerRepository;

    @Override
    public List<Wager> extract(LocalDate date) {
        return wagerRepository.findByWagerTimestamp(date);
    }

    @Override
    public List<Wager> extractAll() {
        return wagerRepository.findAll();
    }
}
