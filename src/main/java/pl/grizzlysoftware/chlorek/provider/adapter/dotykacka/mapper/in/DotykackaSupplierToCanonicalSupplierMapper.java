package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.dotykacka.client.v2.model.Supplier;

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
        out.vatNumber = in.vatId;
        out.vatId = in.vatId;
        out.etag = in.etag;
//        out.email = in.email;
        out.name = in.name;

        if (in.contact != null) {
            out.phone = in.contact.phoneNumber;
        }

        if (in.address != null) {
            out.address1 = in.address.addressLine1;
            out.address2 = in.address.addressLine2;
            out.countryCode = in.address.country;
            out.city = in.address.city;
            out.zipCode = in.address.zipCode;
            out.isDeleted = in.isDeleted;
        }
        return out;
    }
}
