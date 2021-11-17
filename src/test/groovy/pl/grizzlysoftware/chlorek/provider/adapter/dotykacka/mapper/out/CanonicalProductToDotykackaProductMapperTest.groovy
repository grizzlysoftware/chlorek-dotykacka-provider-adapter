package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out

import pl.grizzlysoftware.chlorek.core.model.KeyValueTag
import pl.grizzlysoftware.chlorek.core.model.Product
import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider
import pl.grizzlysoftware.chlorek.core.resolver.ChlorekCsvTagWriter
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductToCanonicalProductMapper
import spock.lang.Specification
import spock.lang.Unroll

import static pl.grizzlysoftware.commons.NumberUtils.with2Scale

class CanonicalProductToDotykackaProductMapperTest extends Specification {
    def "returns null for given null input"() {
        given:
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def out = m.apply(null)
        then:
            out == null
    }

    def "maps id properly"() {
        given:
            def input = new Product()
            input.id = 10L
            def m = new CanonicalProductToDotykackaProductMapper()
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
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.categoryId == input.categoryId
    }


    def "maps etag properly"() {
        given:
            def input = new Product()
            input.etag = UUID.randomUUID().toString()
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.etag == input.etag
    }


    def "maps currency properly"() {
        given:
            def input = new Product()
            input.currency = "PLN"
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.currency == input.currency
    }

    def "maps name properly"() {
        given:
            def input = new Product()
            input.name = 10L
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.name == input.name
    }

    def "maps points properly"() {
        given:
            def input = new Product()
            input.points = 25.3d
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.points == input.points
    }

    @Unroll
    def "maps grossSellPrice properly"() {
        given:
            def input = new Product()
            input.grossSellPrice = 10.31
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.priceWithVat == input.grossSellPrice
    }

    @Unroll
    def "maps netSellPrice properly"() {
        given:
            def input = new Product()
            input.netSellPrice = 23.81
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.priceWithoutVat == input.netSellPrice
    }

    def "maps ean properly"() {
        given:
            def input = new Product()
            input.ean = "ean"
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.eans == [input.ean]
    }

    def "maps minMargin properly"() {
        given:
            def input = new Product()
            input.minMargin = 20
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.marginMin == input.minMargin
    }

    def "maps margin properly"() {
        given:
            def input = new Product()
            input.margin = 0.2351
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.margin == with2Scale(input.margin * 100).doubleValue() + "%"
    }

    def "maps flatMargin properly"() {
        given:
            def input = new Product()
            input.flatMargin = 23.89
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.margin == input.flatMargin.doubleValue() + ""
    }

    @Unroll
    def "maps vat properly"(vat, expectedVat) {
        given:
            def input = new Product()
            input.vat = vat
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.vat == expectedVat
        where:
            vat  || expectedVat
            0    || 1
            0.23 || 1.23
            0.05 || 1.05
    }

    def "invokes DotykackaVatToVatMapper while mapping vat"() {
        given:
            def input = new Product()
            input.vat = 0.23
            def m = new CanonicalProductToDotykackaProductMapper()
            m.toDotykackaVatMapper = Mock(CanonicalVatToDotykackaVatMapper)
        when:
            def output = m.apply(input)
        then:
            1 * m.toDotykackaVatMapper.apply(_) >> 1.23
    }

    def "maps tags properly"() {
        given:
            def input = new Product()
            input.tags = [new KeyValueTag("t1", "99"), new KeyValueTag("t2"), new KeyValueTag("t3", "52"), new KeyValueTag("t4")]
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.tags == ["t1:99", "t2", "t3:52", "t4"]
    }

    @Unroll
    def "maps isDiscountAllowed to isDiscountPermitted"(isDiscountAllowed, expectedOutput) {
        given:
            def input = new Product()
            input.isDiscountAllowed = isDiscountAllowed
            def m = new CanonicalProductToDotykackaProductMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.isDiscountable == expectedOutput
        where:
            isDiscountAllowed || expectedOutput
            false             || false
            true              || true
    }

    def "invokes TagWriter while tags mapping"() {
        given:
            def input = new Product()
            input.tags = [new KeyValueTag("t1", "99"), new KeyValueTag("t2"), new KeyValueTag("t3", "52"), new KeyValueTag("t4")]
            def m = new CanonicalProductToDotykackaProductMapper()
            m.tagWriter = Mock(ChlorekCsvTagWriter)
        when:
            def output = m.apply(input)
        then:
            output != null
            input.tags.size() * m.tagWriter.apply(_)
    }
}
