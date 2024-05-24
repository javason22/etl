package demo.etl.service;

import demo.etl.entity.input.Wager;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WagerService {

    private final WagerRepository wagerRepository;

    @Cacheable(value = "wagerList", key = "#root.methodName + '_' + #example.hashCode() + '_' + #page + '_' + #size")
    public Page<Wager> page(Wager example, int page, int size){
        Sort sort = Sort.by(Sort.Order.asc("accountId"), Sort.Order.desc("wagerTimestamp"));
        return wagerRepository.findAll(Example.of(example), PageRequest.of(page, size, sort));
    }

    @CacheEvict(value = "wagerList", allEntries = true)
    public Wager add(Wager wager){
        return wagerRepository.save(wager);
    }

    @CachePut(value = "wager", key = "#wager.id")
    @CacheEvict(value = "wagerList", allEntries = true)
    public Wager update(Wager wager){
        return wagerRepository.save(wager);
    }

    @CacheEvict(value = "wagerList", allEntries = true)
    public void delete(String id){
        wagerRepository.deleteById(id);
    }

    @Cacheable(value = "wager", key = "#id")
    public Wager get(String id){
        return wagerRepository.findById(id).orElse(null);
    }

}
