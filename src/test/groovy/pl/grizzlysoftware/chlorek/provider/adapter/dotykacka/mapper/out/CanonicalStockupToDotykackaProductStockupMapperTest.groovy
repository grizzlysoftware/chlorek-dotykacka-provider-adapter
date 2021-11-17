package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out

import pl.grizzlysoftware.chlorek.core.model.Invoice
import pl.grizzlysoftware.chlorek.core.model.InvoiceItem
import pl.grizzlysoftware.chlorek.core.model.Stockup
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalInvoiceItemToDotykackaProductStockupItemMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalStockupToDotykackaProductStockupMapper
import spock.lang.Specification
import spock.lang.Unroll

class CanonicalStockupToDotykackaProductStockupMapperTest extends Specification {
    def "returns null if given arg is null"() {
        given:
            def m = new CanonicalStockupToDotykackaProductStockupMapper()
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    def "maps properly"() {
        given:
            def input = new Stockup()
            input.warehouseId = 123131L
            input.notes = "NOTES"
            input.invoice = new Invoice()
            input.invoice.number = "11ddxx"
            input.invoice.supplierId = 99L
            input.invoice.items = [Mock(InvoiceItem), Mock(InvoiceItem), Mock(InvoiceItem)]
            def m = new CanonicalStockupToDotykackaProductStockupMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.warehouseId == input.warehouseId
            output.note == input.notes
            output.invoiceNumber == input.invoice.number
            output.supplierId == input.invoice.supplierId
            output.items != null
            output.items.size() == input.invoice.items.size()
    }

    def "invokes pl.grizzlysfotware.chlorek.management.provider.adapter.mapper for each InvoiceItem"() {
        given:
            def input = new Stockup()
            input.invoice = new Invoice()
            input.invoice.items = [Mock(InvoiceItem), Mock(InvoiceItem), Mock(InvoiceItem)]
            def m = new CanonicalStockupToDotykackaProductStockupMapper()
            m.mapper = Mock(CanonicalInvoiceItemToDotykackaProductStockupItemMapper) {
                3 * apply(_) >> null
            }
        when:
            def output = m.apply(input)
        then:
            output.items != null
            output.items.size() == input.invoice.items.size()
    }

    @Unroll
    def "Invoice.items are always empty list if input.items are null or empty"(items) {
        given:
            def input = new Stockup()
            input.invoice = new Invoice()
            input.invoice.items = items
            def m = new CanonicalStockupToDotykackaProductStockupMapper()
        when:
            def output = m.apply(input)
        then:
            output.items == []
        where:
            items << [null, []]

    }
}
