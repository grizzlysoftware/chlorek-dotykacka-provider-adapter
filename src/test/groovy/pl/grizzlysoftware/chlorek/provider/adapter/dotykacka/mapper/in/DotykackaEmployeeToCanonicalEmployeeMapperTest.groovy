package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.dotykacka.client.v2.model.Employee
import spock.lang.Specification;

class DotykackaEmployeeToCanonicalEmployeeMapperTest extends Specification {
    def "returns null for given null input"() {
        given:
            def m = new DotykackaEmployeeToCanonicalEmployeeMapper()
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    def "maps properly"() {
        given:
            def input = new Employee()
            input.id = 10L
            input.cloudId = 15L
            input.name = "employee 1"
            input.isEnabled = isEnabled
            input.isDeleted = isDeleted
            input.etag = UUID.randomUUID().toString()
            def m = new DotykackaEmployeeToCanonicalEmployeeMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.id == input.id
            output.cloudId == input.cloudId
            output.etag == input.etag
            output.name == input.name
            output.ean == input.barcode
            output.phone == input.phone
            output.isEnabled == eIsEnabled
            output.isDeleted == eIsDeleted
        where:
            isDeleted | eIsDeleted | isEnabled | eIsEnabled
            null      | false      | null      | false
            false     | false      | false     | false
            true      | true       | true      | true

    }
}
