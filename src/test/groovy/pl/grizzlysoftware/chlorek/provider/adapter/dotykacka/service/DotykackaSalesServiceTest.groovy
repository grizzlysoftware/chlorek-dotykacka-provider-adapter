package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import pl.grizzlysoftware.chlorek.core.model.InvoicePosition
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaOrderItemToCanonicalInvoicePositionMapper
import pl.grizzlysoftware.dotykacka.client.v2.model.OrderItem
import pl.grizzlysoftware.dotykacka.client.v2.facade.OrderItemServiceFacade
import pl.grizzlysoftware.dotykacka.client.v2.model.ResultPage
import spock.lang.Specification

class DotykackaSalesServiceTest extends Specification {
    OrderItemServiceFacade orderItemService
    DotykackaSalesService service

    def setup() {
        def orderItems = [Mock(OrderItem), Mock(OrderItem), Mock(OrderItem), Mock(OrderItem)]
        def result = new ResultPage()
        result.data = orderItems
        orderItemService = Mock(OrderItemServiceFacade) {
            getOrderItemsForTimeRange(_, _, _, _, _) >> result
            getAllOrderItemsForTimeRange(_, _, _) >> orderItems
        }
        service = new DotykackaSalesService(orderItemService)

    }

    def "throws exception when given args are null"() {
        when:
            new DotykackaSalesService(null)
        then:
            thrown(NullPointerException)
    }

    def "gets invoice positions #1"() {
        when:
            def output = service.getInvoicePositions(null, null, 0, 0, null)
        then:
            output != null
            output.size() == 4
    }

    def "gets invoice positions #2"() {
        when:
            def output = service.getInvoicePositions(null, null)
        then:
            output != null
            output.size() == 4
    }
    
    def "invokes to canonical invoice position pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each invoice position"() {
        given:
            service.toCanonicalInvoicePositionMapper = Mock(DotykackaOrderItemToCanonicalInvoicePositionMapper) {
                4 * apply(_) >> new InvoicePosition()
            }
        when:
            def output = service.getInvoicePositions(null, null)
        then:
            output != null
            output.size() == 4
    }
}
