package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.category.Category

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaCategoryTestUtils {
    static def "category"(name) {
        def out = new Category()
        out.name = name
        return out
    }
}
