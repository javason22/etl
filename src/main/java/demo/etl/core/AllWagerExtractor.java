package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
@AllArgsConstructor
public class AllWagerExtractor implements Extractor<Wager, Pageable>{

    private final WagerRepository wagerRepository;
    @Override
    public List<Wager> extract(Pageable pageable) {
        return wagerRepository.findAll(pageable).getContent();
    }
}
