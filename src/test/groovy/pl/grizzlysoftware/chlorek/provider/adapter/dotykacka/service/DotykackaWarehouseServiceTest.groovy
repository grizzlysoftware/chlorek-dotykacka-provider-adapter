package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaWarehouseToCanonicalWarehouseMapper
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.Warehouse
import pl.grizzlysoftware.dotykacka.client.v1.facade.WarehouseServiceFacade
import spock.lang.Specification

class DotykackaWarehouseServiceTest extends Specification {
    def "throws exception when given args are null"() {
        when:
            new DotykackaWarehouseService(null)
        then:
            thrown(NullPointerException)
    }

    def "returns warehouses #1"() {
        given:
            def service = Mock(WarehouseServiceFacade) {
                getAllWarehouses() >> [Mock(Warehouse), Mock(Warehouse), Mock(Warehouse), Mock(Warehouse)]
            }
            def m = new DotykackaWarehouseService(service)
        when:
            def output = m.getWarehouses()
        then:
            output != null
            output.size() == 4
    }

    def "invokes toCanonicalWarehouseMapper pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each warehouse while returning warehouses #1"() {
        given:
            def service = Mock(WarehouseServiceFacade) {
                getAllWarehouses() >> [Mock(Warehouse), Mock(Warehouse), Mock(Warehouse), Mock(Warehouse)]
            }
            def m = new DotykackaWarehouseService(service)
        when:
            m.toCanonicalWarehouseMapper = Mock(DotykackaWarehouseToCanonicalWarehouseMapper) {
                4 * apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Warehouse()
            }
        then:
            def output = m.getWarehouses()
    }

    def "returns warehouses #2"() {
        given:
            def service = Mock(WarehouseServiceFacade) {
                getWarehouses(_, _) >> [Mock(Warehouse), Mock(Warehouse), Mock(Warehouse)]
            }
            def m = new DotykackaWarehouseService(service)
        when:
            def output = m.getWarehouses(0, 0)
        then:
            output != null
            output.size() == 3
    }

    def "invokes toCanonicalWarehouseMapper pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each warehouse while returning warehouses #2"() {
        given:
            def service = Mock(WarehouseServiceFacade) {
                getWarehouses(_, _) >> [Mock(Warehouse), Mock(Warehouse), Mock(Warehouse)]
            }
            def m = new DotykackaWarehouseService(service)
        when:
            m.toCanonicalWarehouseMapper = Mock(DotykackaWarehouseToCanonicalWarehouseMapper) {
                3 * apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Warehouse()
            }
        then:
            def output = m.getWarehouses(0, 0)
    }

    def "returns warehouse by id"() {
        given:
            def service = Mock(WarehouseServiceFacade) {
                getWarehouse(_) >> Mock(Warehouse)
            }
            def m = new DotykackaWarehouseService(service)
        when:
            def output = m.getWarehouse(0)
        then:
            output != null
    }

    def "invokes toCanonicalWarehouseMapper pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each warehouse while returning warehouse by id"() {
        given:
            def service = Mock(WarehouseServiceFacade) {
                getWarehouse(_) >> Mock(Warehouse)
            }
            def m = new DotykackaWarehouseService(service)
        when:
            m.toCanonicalWarehouseMapper = Mock(DotykackaWarehouseToCanonicalWarehouseMapper) {
                1 * apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Warehouse()
            }
        then:
            def output = m.getWarehouse(0)
    }

}
