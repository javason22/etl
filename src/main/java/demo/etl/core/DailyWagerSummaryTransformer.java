package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transformer to do the logic to transform Wager data to WagerSummary
 *
 */
@Component
public class DailyWagerSummaryTransformer implements Transformer<Wager, WagerSummary, LocalDate>{

    @Override
    public List<WagerSummary> transform(List<Wager> data, LocalDate currentDate) {
        // sum the total wager amount group by account id and trimmed wagerTimestamp
        List<WagerSummary> output = data.stream()
                .collect(Collectors.groupingBy(Wager::getAccountId, Collectors.reducing(BigDecimal.ZERO, Wager::getWagerAmount, BigDecimal::add)))
                .entrySet().stream()
                .map(entry -> WagerSummary.builder()
                        .accountId(entry.getKey())
                        .totalWagerAmount(entry.getValue())
                        .build())
                .collect(Collectors.toList());
        output.forEach(wagerSummary -> wagerSummary.setWagerDate(currentDate));
        return output;
    }

}
