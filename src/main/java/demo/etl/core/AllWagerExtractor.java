package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AllWagerExtractor implements Extractor<Wager, Pageable>{

    private final WagerRepository wagerRepository;
    @Override
    public List<Wager> extract(Pageable pageable) {
        // convert unmodifiable Page to List
        return new ArrayList<Wager>(wagerRepository.findAll(pageable).getContent());
    }
}
