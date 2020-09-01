package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Supplier
import spock.lang.Specification

class DotykackaSupplierToCanonicalSupplierMapperTest extends Specification {
    def "returns null for given null input" () {
        given:
            def m = new DotykackaSupplierToCanonicalSupplierMapper()
        when:
            def out = m.apply(null)
        then:
            out == null
    }

    def "maps properly"() {
        given:
            def input = new Supplier()
            input.id = 10L
            input.externalId = 12L
            input.cloudId = 14L
            input.companyId = 16L
            input.vatId = "12331313"
            input.vatNo = "33338111"
            input.name = "Test Supplier 1"
            input.phone = "58888"
            input.address1 = "Address 1"
            input.address2 = "Address 2"
            input.countryCode = "Country Code"
            input.city = "City"
            input.zip = "Zip code"
            input.isDeleted = true
            def m = new DotykackaSupplierToCanonicalSupplierMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.id == input.id
            output.externalId == input.externalId
            output.cloudId == input.cloudId
            output.companyId == input.companyId
            output.vatId == input.vatId
            output.vatNumber == input.vatNo
            output.name == input.name
            output.phone == input.phone
            output.address1 == input.address1
            output.address2 == input.address2
            output.countryCode == input.countryCode
            output.city == input.city
            output.zipCode == input.zip
            output.isDeleted == input.isDeleted
    }
}
