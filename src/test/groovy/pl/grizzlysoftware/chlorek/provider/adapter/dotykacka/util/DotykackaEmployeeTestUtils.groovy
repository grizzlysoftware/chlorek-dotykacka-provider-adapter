package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Employee
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.sales.Shift

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaEmployeeTestUtils {
    static def "employee"(name) {
        def out = new Employee()
        out.name = name
        return out
    }

    static def "shift"() {
        def out = new Shift()
        return out
    }
}
