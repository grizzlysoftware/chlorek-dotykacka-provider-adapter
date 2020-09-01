package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import java.math.BigDecimal;
import java.util.function.Function;

import static pl.grizzlysoftware.commons.NumberUtils.with2Scale;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaVatToVatMapper implements Function<Number, BigDecimal> {
    @Override
    public BigDecimal apply(Number in) {
        if (in == null) {
            return null;
        }

        var value = in.doubleValue();
        if (value < 1) {
            return BigDecimal.valueOf(-1);
        }

        if (value > 2) {
            throw new IllegalArgumentException("VAT value cannot be greater than 100% - "+in);
        }

        var out = with2Scale(value - 1);
        return out;
    }
}
