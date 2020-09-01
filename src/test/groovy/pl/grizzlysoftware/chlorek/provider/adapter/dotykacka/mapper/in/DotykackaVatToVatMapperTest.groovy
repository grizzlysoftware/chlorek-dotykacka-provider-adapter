package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaVatToVatMapperTest extends Specification {
    @Unroll
    def "maps properly"(Double input, Double expectedOutput) {
        given:
            def m = new DotykackaVatToVatMapper()
        when:
            def output = m.apply(input)
        then:
            output == expectedOutput
        where:
            input  | expectedOutput
            null   | null
            -1     | -1
            0      | -1
            0.1    | -1
            1.00   | 0
            1.23   | 0.23
            1.2345 | 0.23
    }

    @Unroll
    def "throws illegal argument exception if given value does not match VAT criteria value between [0, 100]%"(input) {
        given:
            def m = new DotykackaVatToVatMapper()
        when:
            m.apply(input)
        then:
            thrown(IllegalArgumentException)
        where:
            input << [10d, 23d]
    }
}
