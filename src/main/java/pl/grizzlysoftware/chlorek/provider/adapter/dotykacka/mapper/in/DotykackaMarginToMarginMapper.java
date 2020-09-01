package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.function.Function;

import static java.lang.Float.parseFloat;
import static pl.grizzlysoftware.commons.NumberUtils.with2Scale;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
@Slf4j
public class DotykackaMarginToMarginMapper implements Function<String, BigDecimal> {
    @Override
    public BigDecimal apply(String in) {
        if (StringUtils.isEmpty(in) || !in.contains("%")) {
            return BigDecimal.ZERO;
        }

        try {
            return with2Scale(parseFloat(in.replaceAll("%", "")) / 100);
        } catch (Exception e) {
            log.warn("Unable to parse given margin {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
