package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.dotykacka.client.v2.model.Branch;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaBranchToCanonicalBranchMapper implements Function<Branch, pl.grizzlysoftware.chlorek.core.model.Branch> {
    @Override
    public pl.grizzlysoftware.chlorek.core.model.Branch apply(Branch in) {
        if (in == null) {
            return null;
        }

        var out = new pl.grizzlysoftware.chlorek.core.model.Branch();
        out.id = in.id;
        out.etag = in.etag;
        out.cloudId = in.cloudId == null ? null : in.cloudId.intValue();
        out.name = in.name;

        return out;
    }
}
