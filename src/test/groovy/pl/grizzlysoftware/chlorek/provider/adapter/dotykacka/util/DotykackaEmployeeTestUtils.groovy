package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util

import pl.grizzlysoftware.dotykacka.client.v2.model.Employee

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaEmployeeTestUtils {
    static def "employee"(name) {
        def out = new Employee()
        out.name = name
        return out
    }
}
