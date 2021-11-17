package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.dotykacka.client.v2.model.Address
import pl.grizzlysoftware.dotykacka.client.v2.model.Contact
import pl.grizzlysoftware.dotykacka.client.v2.model.Supplier
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
            input.etag = UUID.randomUUID().toString()
            input.name = "Test Supplier 1"
            input.contact = new Contact()
            input.contact.phoneNumber = "58888"
//            input.contact.email = "supplier@test.com"
            input.address = new Address()
            input.address.addressLine1 = "Address 1"
            input.address.addressLine2 = "Address 2"
            input.address.country = "Country"
            input.address.city = "City"
            input.address.zipCode = "Zip code"
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
            output.vatNumber == input.vatId
            output.etag == input.etag
            output.name == input.name
            output.phone == input.contact.phoneNumber
//            output.email == input.contact.email
            output.address1 == input.address.addressLine1
            output.address2 == input.address.addressLine2
            output.countryCode == input.address.country
            output.city == input.address.city
            output.zipCode == input.address.zipCode
            output.isDeleted == input.isDeleted
    }
}
