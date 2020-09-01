package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import java.util.function.BiFunction;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalMarginsToDotykackaMarginMapper implements BiFunction<Number, Number, String> {
    @Override
    public String apply(Number margin, Number flatMargin) {
        if (margin != null) {
            return margin.floatValue() * 100 + "%";
        }

        if (flatMargin != null) {
            return flatMargin.floatValue() + "";
        }

        return null;
    }
}
