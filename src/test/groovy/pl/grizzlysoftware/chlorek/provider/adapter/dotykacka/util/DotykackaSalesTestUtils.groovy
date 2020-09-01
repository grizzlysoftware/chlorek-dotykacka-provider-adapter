package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util


import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Employee

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaSalesTestUtils {

    static def "employee"(name) {
        def out = new Employee()
        out.name = name
        return out
    }
}
