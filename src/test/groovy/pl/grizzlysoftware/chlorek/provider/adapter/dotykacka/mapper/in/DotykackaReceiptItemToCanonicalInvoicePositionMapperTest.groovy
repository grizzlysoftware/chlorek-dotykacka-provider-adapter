package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.SingleStringTagsToCollectionStringTagsMapper
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.sales.ReceiptItem;
import spock.lang.Specification;

class DotykackaReceiptItemToCanonicalInvoicePositionMapperTest extends Specification {
    def "returns null for given null input"() {
        given:
            def m = new DotykackaBranchToCanonicalBranchMapper()
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    def "maps properly"() {
        given:
            def input = new ReceiptItem()
            input.orderId = 10L
            input.cloudId = 11L
            input.branchId = 12L
            input.categoryId = 13L
            input.productId = 14L
            input.customerId = 15L
            input.employeeId = 16L
            input.refundId = 17L
            input.name = "product 1"
            input.ean = "34852345823"

            def m = new DotykackaReceiptItemToCanonicalInvoicePositionMapper()
            m.tagsMapper = Mock(SingleStringTagsToCollectionStringTagsMapper)
        when:
            def output = m.apply(input)
        then:
            output != null
            output.invoiceId == input.orderId
            output.cloudId == input.cloudId
            output.branchId == input.branchId
            output.categoryId == input.categoryId
            output.productId == input.productId
            output.customerId == input.customerId
            output.employeeId == input.employeeId
            output.refundId == input.refundId
            output.name == input.name
            output.ean == input.ean

            1 * m.tagsMapper.apply(_)
    }
}
