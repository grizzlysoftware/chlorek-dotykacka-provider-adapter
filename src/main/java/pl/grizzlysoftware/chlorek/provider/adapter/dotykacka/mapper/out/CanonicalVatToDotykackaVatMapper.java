package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import java.math.BigDecimal;
import java.util.function.Function;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static pl.grizzlysoftware.commons.NumberUtils.with2Scale;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalVatToDotykackaVatMapper implements Function<BigDecimal, Double> {
    @Override
    public Double apply(BigDecimal in) {
        if (in == null) {
            return -1d;//TODO workaround because current dotykacka model does not allow us to provide nulls
        }

        if (ONE.compareTo(in) <= 0) {
            throw new IllegalArgumentException("VAT value cannot be greater than or equal to 100%");
        }

        if (ZERO.compareTo(in) > 0) {
            throw new IllegalArgumentException("VAT value cannot be lesser than 0%");
        }

        var out = with2Scale(in.add(ONE)).doubleValue();
        return out;
    }
}
