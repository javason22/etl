package demo.etl.service;

import demo.etl.entity.input.Wager;
import demo.etl.repository.input.WagerRepository;
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

@Service
@Slf4j
@AllArgsConstructor
public class WagerService {

    private final WagerRepository wagerRepository;

    private final RBloomFilter<String> wagerBloomFilter;

    @Cacheable(value = "wagerList", key = "#root.methodName + '_' + #example.hashCode() + '_' + #page + '_' + #size")
    public Page<Wager> page(Wager example, int page, int size){
        Sort sort = Sort.by(Sort.Order.asc("accountId"), Sort.Order.desc("wagerTimestamp"));
        return wagerRepository.findAll(Example.of(example), PageRequest.of(page, size, sort));
    }

    @CacheEvict(value = "wagerList", allEntries = true)
    public Wager add(Wager wager){
        Wager result = wagerRepository.save(wager);
        // add the wager id to bloom filter in service layer
        wagerBloomFilter.add(wager.getId());
        return result;
    }

    @CachePut(value = "wager", key = "#wager.id")
    @CacheEvict(value = "wagerList", allEntries = true)
    public Wager update(Wager wager){
        // check if the wager id exists in bloom filter
        if(!wagerBloomFilter.contains(wager.getId())){
            log.info("Wager id={} not found in bloom filter", wager.getId());
            return null;
        }
        return wagerRepository.save(wager);
    }

    @CacheEvict(value = "wagerList", allEntries = true)
    public boolean delete(String id){
        // check if the wager id exists in bloom filter
        if(!wagerBloomFilter.contains(id)){
            log.info("Wager id={} not found in bloom filter", id);
            return false;
        }
        if(!wagerRepository.findById(id).isPresent()) {
            return false;
        }
        wagerRepository.deleteById(id);
        return true;
    }

    @Cacheable(value = "wager", key = "#id")
    public Wager get(String id){
        if(!wagerBloomFilter.contains(id)){
            log.info("Wager id={} not found in bloom filter", id);
            return null;
        }
        return wagerRepository.findById(id).orElse(null);
    }

}
