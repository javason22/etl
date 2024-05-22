package demo.etl.service;

import demo.etl.entity.Wager;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WagerService {

    private final WagerRepository wagerRepository;

    @Cacheable(value = "wagerList", key = "#root.methodName + '_' + #example.hashCode() + '_' + #page + '_' + #size")
    public Page<Wager> list(Wager example, int page, int size){
        return wagerRepository.findAll(Example.of(example), PageRequest.of(page, size));
    }

    @CacheEvict(value = "wagerList", allEntries = true)
    public Wager add(Wager wager){
        return wagerRepository.save(wager);
    }

    @CacheEvict(value = "wagerList", allEntries = true)
    public Wager update(Wager wager){
        return wagerRepository.save(wager);
    }

    @CacheEvict(value = "wagerList", allEntries = true)
    public void delete(Long id){
        wagerRepository.deleteById(id);
    }

}
