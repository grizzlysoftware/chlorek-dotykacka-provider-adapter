package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaWarehouseToCanonicalWarehouseMapper
import pl.grizzlysoftware.dotykacka.client.v2.model.ResultPage
import pl.grizzlysoftware.dotykacka.client.v2.model.Warehouse
import pl.grizzlysoftware.dotykacka.client.v2.facade.WarehouseServiceFacade
import spock.lang.Specification

class DotykackaWarehouseServiceTest extends Specification {
    WarehouseServiceFacade service
    DotykackaWarehouseService m

    def setup() {
        def warehouses = [Mock(Warehouse), Mock(Warehouse), Mock(Warehouse)]
        def result = new ResultPage()
        result.data = warehouses
        service = Mock(WarehouseServiceFacade) {
            getWarehouses(_, _) >> result
            getWarehouses(_, _, _) >> result
            getAllWarehouses() >> warehouses
            getWarehouse(_) >> Mock(Warehouse)
        }
        m = new DotykackaWarehouseService(service)
    }

    def "throws exception when given args are null"() {
        when:
            new DotykackaWarehouseService(null)
        then:
            thrown(NullPointerException)
    }

    def "returns warehouses #1"() {
        when:
            def output = m.getWarehouses()
        then:
            output != null
            output.size() == 3
    }

    def "invokes toCanonicalWarehouseMapper pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each warehouse while returning warehouses #1"() {
        when:
            m.toCanonicalWarehouseMapper = Mock(DotykackaWarehouseToCanonicalWarehouseMapper) {
                3 * apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Warehouse()
            }
        then:
            def output = m.getWarehouses()
    }

    def "returns warehouses #2"() {
        when:
            def output = m.getWarehouses(0, 0)
        then:
            output != null
            output.size() == 3
    }

    def "invokes toCanonicalWarehouseMapper pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each warehouse while returning warehouses #2"() {
        when:
            m.toCanonicalWarehouseMapper = Mock(DotykackaWarehouseToCanonicalWarehouseMapper) {
                3 * apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Warehouse()
            }
        then:
            def output = m.getWarehouses(0, 0)
    }

    def "returns warehouse by id"() {
        when:
            def output = m.getWarehouse(0)
        then:
            output != null
    }

    def "invokes toCanonicalWarehouseMapper pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each warehouse while returning warehouse by id"() {
        when:
            m.toCanonicalWarehouseMapper = Mock(DotykackaWarehouseToCanonicalWarehouseMapper) {
                1 * apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Warehouse()
            }
        then:
            def output = m.getWarehouse(0)
    }

}
