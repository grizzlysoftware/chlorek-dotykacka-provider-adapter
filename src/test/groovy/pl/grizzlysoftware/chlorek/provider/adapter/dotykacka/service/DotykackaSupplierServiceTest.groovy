package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import org.apache.commons.lang3.StringUtils
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaSupplierToCanonicalSupplierMapper
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Supplier
import pl.grizzlysoftware.dotykacka.client.v1.facade.SupplierServiceFacade;
import spock.lang.Specification

import static java.util.stream.Collectors.toList
import static pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.DotykackaSupplierTestUtils.supplier;

class DotykackaSupplierServiceTest extends Specification {
    def "throws exception when given args are null"() {
        when:
            new DotykackaSupplierService(null)
        then:
            thrown(NullPointerException)
    }

    def "returns suppliers"() {
        given:
            def service = Mock(SupplierServiceFacade) {
                getAllSuppliers() >> [Mock(Supplier), Mock(Supplier), Mock(Supplier), Mock(Supplier)]
            }
            def m = new DotykackaSupplierService(service)
        when:
            def output = m.getSuppliers()
        then:
            output != null
            output.size() == 4
    }

    def "returned suppliers are sorted alphabetically ascending"() {
        given:
            def service = Mock(SupplierServiceFacade) {
                getAllSuppliers() >> [supplier("b"), supplier("c"), supplier("a"), supplier("e")]
            }
            def m = new DotykackaSupplierService(service)
        when:
            def output = m.getSuppliers()
            def sorted = output.stream()
                    .sorted({ e1, e2 -> StringUtils.compare(e1.name, e2.name) })
                    .collect(toList())
        then:
            output == sorted
    }

    def "invokes toCanonicalSupplierMapper for each fetched supplier"() {
        given:
            def service = Mock(SupplierServiceFacade) {
                getAllSuppliers() >> [Mock(Supplier), Mock(Supplier), Mock(Supplier), Mock(Supplier)]
            }
            def m = new DotykackaSupplierService(service)
        when:
            m.toCanonicalSupplierMapper = Mock(DotykackaSupplierToCanonicalSupplierMapper) {
                4 * apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Supplier()
            }
        then:
            def output = m.getSuppliers()
    }
}
