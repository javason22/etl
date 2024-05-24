package demo.etl.service;

import demo.etl.entity.output.WagerSummary;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WagerSummaryService {

    private final WagerSummaryRepository wagerSummaryRepository;

    @Cacheable(value = "wagerSummaryList", key = "#root.methodName + '_' + #example.hashCode() + '_' + #page + '_' + #size")
    public Page<WagerSummary> page(WagerSummary example, int page, int size){
        Sort sort = Sort.by(Sort.Order.asc("accountId"), Sort.Order.desc("wagerDate"));
        return wagerSummaryRepository.findAll(Example.of(example), PageRequest.of(page, size, sort));
    }

    @CacheEvict(value = "wagerSummaryList", allEntries = true)
    public WagerSummary add(WagerSummary wagerSummary){
        return wagerSummaryRepository.save(wagerSummary);
    }

    @CacheEvict(value = "wagerSummaryList", allEntries = true)
    public WagerSummary update(WagerSummary wagerSummary){
        return wagerSummaryRepository.save(wagerSummary);
    }

    @CacheEvict(value = "wagerSummaryList", allEntries = true)
    public void delete(String id){
        wagerSummaryRepository.deleteById(id);
    }

    @Cacheable(value = "wagerSummary", key = "#id")
    public WagerSummary get(String id){
        return wagerSummaryRepository.findById(id).orElse(null);
    }
}
