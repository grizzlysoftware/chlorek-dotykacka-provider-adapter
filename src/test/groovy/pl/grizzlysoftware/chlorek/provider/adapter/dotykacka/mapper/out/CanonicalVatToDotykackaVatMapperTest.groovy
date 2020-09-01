package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out


import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class CanonicalVatToDotykackaVatMapperTest extends Specification {
    @Unroll
    def "maps properly"(Double input, Double expectedOutput) {
        given:
            def m = new CanonicalVatToDotykackaVatMapper()
        when:
            def output = m.apply(input)
        then:
            output == expectedOutput
        where:
            input   | expectedOutput
            null    | -1
            0.00    | 1.00
            0.05    | 1.05
            0.08    | 1.08
            0.23    | 1.23
            0.23123 | 1.23
            0.99    | 1.99
            0.99123 | 1.99
    }

    @Unroll
    def "throws illegal argument exception if given value does not match VAT criteria value between [0, 100)%"(input) {
        given:
            def m = new CanonicalVatToDotykackaVatMapper()
        when:
            m.apply(input)
        then:
            thrown(IllegalArgumentException)
        where:
            input << [-10.0d, -1.0d, -0.1d, 1.0d, 10d, 23d]
    }
}
