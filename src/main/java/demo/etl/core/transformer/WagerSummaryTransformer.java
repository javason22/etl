package demo.etl.core.transformer;

import demo.etl.core.transformer.Transformer;
import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Transformer to do the logic to transform Wager data to WagerSummary
 *
 */
@Component
public class WagerSummaryTransformer implements Transformer<Wager, WagerSummary> {

    @Override
    public List<WagerSummary> transform(List<Wager> data) {
        List<WagerSummary> output = new ArrayList<>();
        if(data != null && !data.isEmpty()){
            return output; // return empty list if data is empty
        }
        // group by accountId to a map
        Map<String, List<Wager>> wagerMap = data.stream()
                .collect(Collectors.groupingBy(wager -> wager.getAccountId()));
        // calculate totalWagerAmount for each accountId
        wagerMap.forEach((accountId, wagers) -> {
            // group by wagerDate to a map
            Map<LocalDate, List<Wager>> wagerDateMap = wagers.stream()
                    .collect(Collectors.groupingBy(wager -> wager.getWagerTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            // calculate totalWagerAmount for each wagerDate for each accountId
            List<WagerSummary> wagerSummaries = wagerDateMap.entrySet().stream()
                    .map(entry -> {
                        BigDecimal totalWagerAmount = entry.getValue().stream()
                                .map(Wager::getWagerAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        return WagerSummary.builder()
                                .accountId(accountId)
                                .totalWagerAmount(totalWagerAmount)
                                .wagerDate(entry.getKey())
                                .build();
                    })
                    .collect(Collectors.toList());
            output.addAll(wagerSummaries);
        });
        return output;
    }

}
