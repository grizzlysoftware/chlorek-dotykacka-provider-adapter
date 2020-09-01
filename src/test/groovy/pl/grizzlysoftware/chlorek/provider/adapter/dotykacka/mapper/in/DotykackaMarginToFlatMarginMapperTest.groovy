package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaMarginToFlatMarginMapperTest extends Specification {
    @Unroll
    def "maps properly"(input, expectedOutput) {
        given:
            def m = new DotykackaMarginToFlatMarginMapper()
        when:
            def output = m.apply(input)
        then:
            output == expectedOutput
        where:
            input   | expectedOutput
            null    | 0
            ""      | 0
            "x"     | 0
            "%"     | 0
            "5%"    | 0
            "5.1"   | 5.1
            "5.123" | 5.12
            "90"    | 90
    }
}
