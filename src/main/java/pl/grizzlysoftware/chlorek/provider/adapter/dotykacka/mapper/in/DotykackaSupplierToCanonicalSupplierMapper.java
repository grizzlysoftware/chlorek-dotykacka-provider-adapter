package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Supplier;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaSupplierToCanonicalSupplierMapper implements Function<Supplier, pl.grizzlysoftware.chlorek.core.model.Supplier> {
    @Override
    public pl.grizzlysoftware.chlorek.core.model.Supplier apply(Supplier in) {
        if (in == null) {
            return null;
        }

        var out = new pl.grizzlysoftware.chlorek.core.model.Supplier();
        out.id = in.id;
        out.externalId = in.externalId;
        out.cloudId = in.cloudId == null ? null : in.cloudId.intValue();
        out.companyId = in.companyId;
        out.vatId = in.vatId;
        out.vatNumber = in.vatNo;
        out.name = in.name;
        out.phone = in.phone;
        out.address1 = in.address1;
        out.address2 = in.address2;
        out.countryCode = in.countryCode;
        out.city = in.city;
        out.zipCode = in.zip;
        out.isDeleted = in.isDeleted == null ? false : in.isDeleted;
        return out;
    }
}
