package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.sales.Shift;

import java.util.function.Function;

import static pl.grizzlysoftware.commons.TimeUtils.fromMillis;


/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaShiftToCanonicalShiftMapper implements Function<Shift, pl.grizzlysoftware.chlorek.core.model.Shift> {
    @Override
    public pl.grizzlysoftware.chlorek.core.model.Shift apply(Shift in) {
        if (in == null) {
            return null;
        }
        var out = new pl.grizzlysoftware.chlorek.core.model.Shift();
        out.startTime = fromMillis(in.startTime);
        out.endTime = fromMillis(in.endTime);
        out.employeeName = in.employeeName;

        return out;
    }
}
