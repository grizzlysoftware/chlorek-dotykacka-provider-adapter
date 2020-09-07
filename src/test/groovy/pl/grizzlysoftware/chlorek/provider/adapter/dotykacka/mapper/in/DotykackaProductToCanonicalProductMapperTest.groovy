package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.chlorek.core.model.ContainerType
import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductToDotykackaProductMapper
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.product.Product
import spock.lang.Specification
import spock.lang.Unroll

import static pl.grizzlysoftware.chlorek.core.model.TagRegistry.CONTAINER_TYPE_TAG

class DotykackaProductToCanonicalProductMapperTest extends Specification {
    def "returns null for given null input"() {
        given:
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    def "maps id properly"() {
        given:
            def input = new Product()
            input.id = 10L
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.id == input.id
    }

    def "maps categoryId properly"() {
        given:
            def input = new Product()
            input.categoryId = 10L
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.categoryId == input.categoryId
            1 * m.categoryProvider.getCategory(_)
    }

    def "maps name properly"() {
        given:
            def input = new Product()
            input.name = 10L
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.name == input.name
    }

    def "maps points properly"() {
        given:
            def input = new Product()
            input.points = 23.2d
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.points == input.points
    }

    @Unroll
    def "maps grossPrice properly"(grossPrice, expectedGrossPrice) {
        given:
            def input = new Product()
            input.grossPrice = grossPrice
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.grossSellPrice == expectedGrossPrice
        where:
            grossPrice | expectedGrossPrice
            10         | 10
            1.2345     | 1.23
    }

    @Unroll
    def "maps netPrice properly"(netPrice, expectedNetPrice) {
        given:
            def input = new Product()
            input.netPrice = netPrice
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.netSellPrice == expectedNetPrice
        where:
            netPrice | expectedNetPrice
            10       | 10
            1.2345   | 1.23
    }

    def "maps ean properly"() {
        given:
            def input = new Product()
            input.ean = "ean"
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.ean == input.ean
    }

    def "maps minMargin properly"() {
        given:
            def input = new Product()
            input.marginMin = 20
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
            m.toFlatMarginMapper = Mock(DotykackaMarginToFlatMarginMapper)
            m.toMarginMapper = Mock(DotykackaMarginToMarginMapper)
        when:
            def output = m.apply(input)
        then:
            output != null
            output.minMargin == input.marginMin
            1 * m.toFlatMarginMapper.apply(_)
    }

    def "maps margin properly"() {
        given:
            def input = new Product()
            input.margin = "20%"
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.margin == 0.20
    }

    def "invokes DotykackaMarginToMarginMapper while mapping margin"() {
        given:
            def input = new Product()
            input.margin = "20.5"
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
            m.toMarginMapper = Mock(DotykackaMarginToMarginMapper)
        when:
            def output = m.apply(input)
        then:
            1 * m.toMarginMapper.apply(_)
    }

    def "maps margin(flat) properly"() {
        given:
            def input = new Product()
            input.margin = "20.5"
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output != null
            output.flatMargin == Float.parseFloat(input.margin)
    }

    def "invokes DotykackaMarginToFlatMarginMapper while mapping margin(flat)"() {
        given:
            def input = new Product()
            input.margin = "20.5"
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
            m.toFlatMarginMapper = Mock(DotykackaMarginToFlatMarginMapper)
        when:
            def output = m.apply(input)
        then:
            1 * m.toFlatMarginMapper.apply(_)
    }

    def "maps vat properly"(vat, expectedVat) {
        given:
            def input = new Product()
            input.vat = vat
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
            m.toVatMapper = Spy(DotykackaVatToVatMapper) {
                apply(_) >> { s -> apply(s) }
            }
        when:
            def output = m.apply(input)
        then:
            output != null
            output.vat == expectedVat
            1 * m.toVatMapper.apply(_)
        where:
            vat  || expectedVat
            1    || 0
            1.23 || 0.23
            1.05 || 0.05
    }

    def "maps tagsList properly"() {
        given:
            def input = new Product()
            input.tagsList = "t1:99,t2:,t3:52,t4"
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
            m.toTagsMapper = Spy(DotykackaSingleStringTagsToCollectionStringTagsMapper) {
                apply(_) >> { s -> apply(s) }
            }
        when:
            def output = m.apply(input)
        then:
            output != null
            output.tags != null
            output.tags.size() == 4
            output.tags[0].name == "t1"
            output.tags[0].value == "99"
            output.tags[1].name == "t2"
            output.tags[1].value == null
            output.tags[2].name == "t3"
            output.tags[2].value == "52"
            output.tags[3].name == "t4"
            output.tags[3].value == null
            1 * m.toTagsMapper.apply(_)
    }

    @Unroll
    def "resolves containerType from tags"(containerType) {
        given:
            def input = new Product()
            input.tagsList = "${CONTAINER_TYPE_TAG}:${containerType}"
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
        when:
            def output = m.apply(input)
        then:
            output.containerType == containerType
        where:
            containerType << ContainerType.values()
    }

    @Unroll
    def "maps isDiscountPermitted to isDiscountAllowed"(isDiscountPermitted, expectedOutput) {
        given:
            def input = new Product()
            input.isDiscountPermitted = isDiscountPermitted
            def m = new DotykackaProductToCanonicalProductMapper(Mock(CategoryProvider))
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
