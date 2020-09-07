package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.SingleStringTagsToCollectionStringTagsMapper
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.product.ProductWithStockStatus
import spock.lang.Specification
import spock.lang.Unroll

class DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapperTest extends Specification {
    def "throws NullPointerException when null arg is given"() {
        when:
            new DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper(null)
        then:
            thrown(NullPointerException)
    }

    def "returns null for given null input"() {
        given:
            def m = new DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper(
                    Mock(CategoryProvider)
            )
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    //TODO refactor this shit before next release - 1.0.2
    def "maps properly"() {
        given:
            def input = new ProductWithStockStatus()
            input.id = 5L
            input.categoryId = 15L
            input.name = "product "
            input.points = 55
            input.ean = "34582345"
            input.plu = "3213"
            input.grossPrice = 12.551
            input.netPrice = 123.551
            input.vat = 1.23
            input.tagsList = "123, 123, 123, 5"
            input.marginMin = 55
            input.isDeleted = true
            input.margin = "55%"
            input.lastInventoryValue = 99
            input.lastPurchaseNetPrice = 55.123
            input.stockQuantityStatus = 101
            def categoryProvider = Mock(CategoryProvider) {
                1 * getCategory(_) >> {
                    def out = new pl.grizzlysoftware.chlorek.core.model.Category()
                    out.name = "C1"
                    return out
                }
            }
            def toMarginMapper = Mock(DotykackaMarginToMarginMapper) {
                1 * apply(_) >> 1
            }
            def toFlatMarginMapper = Mock(DotykackaMarginToFlatMarginMapper) {
                1 * apply(_) >> 2
            }
            def tagsMapper = Mock(SingleStringTagsToCollectionStringTagsMapper) {
                1 * apply(_) >> new ArrayList<>()
            }
            def realToVatMapper = new DotykackaVatToVatMapper()
            def toVatMapper = Mock(DotykackaVatToVatMapper) {
                1 * apply(_) >> { args -> realToVatMapper.apply(args[0]) }
            }

            def m = new DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper(categoryProvider)
            m.toStringTagMapper = tagsMapper
            m.toMarginMapper = toMarginMapper
            m.toFlatMarginMapper = toFlatMarginMapper
            m.toVatMapper = toVatMapper
        when:
            def output = m.apply(input)
        then:
            output != null
            output.id == input.id
            output.categoryId == input.categoryId
            output.categoryName == "C1"
            output.name == input.name
            output.points == input.points
            output.ean == input.ean
            output.plu == input.plu
            output.grossSellPrice == 12.55
            output.netSellPrice == 123.55
            output.vat == realToVatMapper.apply(input.vat)
            output.tags != null
            output.minMargin == input.marginMin
            output.isDeleted == input.isDeleted
            output.margin == 1
            output.flatMargin == 2
            output.lastInventoryValue == input.lastInventoryValue
            output.lastPurchaseNetPrice == 55.12
            output.stockQuantity == input.stockQuantityStatus
    }

    @Unroll
    def "maps isDiscountPermitted to isDiscountAllowed"(isDiscountPermitted, expectedOutput) {
        given:
            def input = new ProductWithStockStatus()
            input.isDiscountPermitted = isDiscountPermitted
            def m = new DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.isDiscountAllowed == expectedOutput
        where:
            isDiscountPermitted | expectedOutput
            null                | false
            false               | false
            true                | true
    }
}
