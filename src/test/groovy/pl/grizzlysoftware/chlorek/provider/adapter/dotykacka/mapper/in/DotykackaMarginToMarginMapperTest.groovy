package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaMarginToMarginMapperTest extends Specification {
    @Unroll
    def "maps properly"(input, expectedOutput) {
        given:
            def m = new DotykackaMarginToMarginMapper()
        when:
            def output = m.apply(input)
        then:
            output == expectedOutput
        where:
            input    | expectedOutput
            null     | 0
            ""       | 0
            "x"      | 0
            "%"      | 0
            "5.1"    | 0
            "90"     | 0
            "0.123%" | 0.00
            "5%"     | 0.05
            "95.25%" | 0.95
    }
}
