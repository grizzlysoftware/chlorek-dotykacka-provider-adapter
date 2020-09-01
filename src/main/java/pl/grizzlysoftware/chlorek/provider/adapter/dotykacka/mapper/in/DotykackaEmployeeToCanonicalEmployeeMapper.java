package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Employee;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaEmployeeToCanonicalEmployeeMapper implements Function<Employee, pl.grizzlysoftware.chlorek.core.model.Employee> {
    @Override
    public pl.grizzlysoftware.chlorek.core.model.Employee apply(Employee in) {
        if (in == null) {
            return null;
        }

        var out = new pl.grizzlysoftware.chlorek.core.model.Employee();
        out.id = in.id;
        out.cloudId = in.cloudId == null ? null : in.cloudId.intValue();
        out.name = in.name;
        out.ean = in.barcode;
        out.phone = in.phone;
        out.isEnabled = in.isEnabled == null ? false : in.isEnabled;
        out.isDeleted = in.isDeleted == null ? false : in.isDeleted;
        return out;
    }
}
