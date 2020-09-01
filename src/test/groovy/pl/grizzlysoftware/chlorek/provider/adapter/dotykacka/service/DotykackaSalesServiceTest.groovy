package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import pl.grizzlysoftware.chlorek.core.model.InvoicePosition
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaReceiptItemToCanonicalInvoicePositionMapper
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.sales.ReceiptItem
import pl.grizzlysoftware.dotykacka.client.v1.facade.SalesServiceFacade
import spock.lang.Specification

class DotykackaSalesServiceTest extends Specification {
    def "throws exception when given args are null"() {
        when:
            new DotykackaSalesService(null)
        then:
            thrown(NullPointerException)
    }

    def "gets invoice positions #1"() {
        given:
            def service = Mock(SalesServiceFacade) {
                getReceiptItems(_, _, _, _, _) >> [Mock(ReceiptItem), Mock(ReceiptItem), Mock(ReceiptItem), Mock(ReceiptItem)]
            }
            def m = new DotykackaSalesService(service)
        when:
            def output = m.getInvoicePositions(null, null, 0, 0, null)
        then:
            output != null
            output.size() == 4
    }

    def "gets invoice positions #2"() {
        given:
            def service = Mock(SalesServiceFacade) {
                getReceiptItems(_, _) >> [Mock(ReceiptItem), Mock(ReceiptItem), Mock(ReceiptItem), Mock(ReceiptItem)]
            }
            def m = new DotykackaSalesService(service)
        when:
            def output = m.getInvoicePositions(null, null)
        then:
            output != null
            output.size() == 4
    }
    
    def "invokes to canonical invoice position pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each invoice position"() {
        given:
            def service = Mock(SalesServiceFacade) {
                getReceiptItems(_, _) >> [Mock(ReceiptItem), Mock(ReceiptItem), Mock(ReceiptItem), Mock(ReceiptItem)]
            }
            def m = new DotykackaSalesService(service)
            m.toCanonicalInvoicePositionMapper = Mock(DotykackaReceiptItemToCanonicalInvoicePositionMapper) {
                4 * apply(_) >> new InvoicePosition()
            }
        when:
            def output = m.getInvoicePositions(null, null)
        then:
            output != null
            output.size() == 4
    }
}
