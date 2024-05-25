package demo.etl.service;

import demo.etl.entity.output.WagerSummary;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class WagerSummaryService {

    private final WagerSummaryRepository wagerSummaryRepository;

    private final RBloomFilter<String> wagerSummaryBloomFilter;

    @Cacheable(value = "wagerSummaryList", key = "#root.methodName + '_' + #example.hashCode() + '_' + #page + '_' + #size")
    public Page<WagerSummary> page(WagerSummary example, int page, int size){
        Sort sort = Sort.by(Sort.Order.asc("accountId"), Sort.Order.asc("wagerDate"));
        return wagerSummaryRepository.findAll(Example.of(example), PageRequest.of(page, size, sort));
    }

    @CacheEvict(value = "wagerSummaryList", allEntries = true, condition = "#result != null")
    public WagerSummary add(WagerSummary wagerSummary){
        WagerSummary result = wagerSummaryRepository.save(wagerSummary);
        // add the wager summary id to bloom filter in service layer
        wagerSummaryBloomFilter.add(wagerSummary.getId());
        return result;
    }

    @CachePut(value = "wagerSummary", key = "#wagerSummary.id", unless = "#result == null")
    @CacheEvict(value = "wagerSummaryList", allEntries = true, condition = "#result != null")
    public WagerSummary update(WagerSummary wagerSummary){
        // check if the wager summary id exists in bloom filter
        if(!wagerSummaryBloomFilter.contains(wagerSummary.getId())){
            log.info("Wager summary id={} not found in bloom filter", wagerSummary.getId());
            return null;
        }
        return wagerSummaryRepository.save(wagerSummary);
    }

    @CacheEvict(value = "wagerSummaryList", allEntries = true, condition = "#result")
    public boolean delete(String id){
        // check if the wager summary id exists in bloom filter
        if(!wagerSummaryBloomFilter.contains(id)){
            log.info("Wager summary id={} not found in bloom filter", id);
            return false;
        }
        if(!wagerSummaryRepository.findById(id).isPresent()) {
            return false;
        }
        wagerSummaryRepository.deleteById(id);
        return true;
    }

    @Cacheable(value = "wagerSummary", key = "#id", unless = "#result == null")
    public WagerSummary get(String id){
        // check if the wager summary id exists in bloom filter
        if(!wagerSummaryBloomFilter.contains(id)){
            log.info("Wager summary id={} not found in bloom filter", id);
            return null;
        }
        return wagerSummaryRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "wagerSummaryList", allEntries = true, condition = "#result != null")
    public List<WagerSummary> saveAll(List<WagerSummary> wagerSummaries){
        List<WagerSummary> result = wagerSummaryRepository.saveAll(wagerSummaries);
        // add the wager summary id to bloom filter in service layer
        result.forEach(wagerSummary -> wagerSummaryBloomFilter.add(wagerSummary.getId()));
        return result;
    }
}
