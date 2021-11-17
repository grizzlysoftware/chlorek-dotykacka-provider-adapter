package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.dotykacka.client.v2.model.Warehouse
import spock.lang.Specification

class DotykackaWarehouseToCanonicalWarehouseMapperTest extends Specification {
    def "returns null for given null input" () {
        given:
            def m = new DotykackaWarehouseToCanonicalWarehouseMapper()
        when:
            def out = m.apply(null)
        then:
            out == null
    }

    def "maps properly"() {
        given:
            def input = new Warehouse()
            input.id = 10L
            input.cloudId = 15L
            input.name = "Warehouse 1"
            def m = new DotykackaWarehouseToCanonicalWarehouseMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.id == input.id
            output.cloudId == input.cloudId
            output.name == input.name
    }
}
