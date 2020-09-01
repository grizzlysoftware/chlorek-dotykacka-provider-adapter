package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Supplier

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaSupplierTestUtils {
    static def "supplier"(name) {
        def out = new Supplier()
        out.name = name
        return out
    }
}
