package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Warehouse;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaWarehouseToCanonicalWarehouseMapper implements Function<Warehouse, pl.grizzlysoftware.chlorek.core.model.Warehouse> {
    @Override
    public pl.grizzlysoftware.chlorek.core.model.Warehouse apply(Warehouse in) {
        if (in == null) {
            return null;
        }

        var out = new pl.grizzlysoftware.chlorek.core.model.Warehouse();
        out.id = in.id;
        out.cloudId = in.cloudiId;
        out.name = in.name;
        return out;
    }
}
