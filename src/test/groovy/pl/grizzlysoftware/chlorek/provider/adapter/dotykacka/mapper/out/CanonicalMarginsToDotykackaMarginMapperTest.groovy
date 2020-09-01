package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out


import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class CanonicalMarginsToDotykackaMarginMapperTest extends Specification {
    @Unroll
    def "maps properly"(margin, flatMargin, expectedOutput) {
        given:
            def m = new CanonicalMarginsToDotykackaMarginMapper()
        when:
            def output = m.apply(margin, flatMargin)
        then:
            output == expectedOutput
        where:
            margin | flatMargin | expectedOutput
            null   | null       | null
            0.1399 | null       | "13.99%"
            null   | 12.99      | "12.99"
            0.1399 | 12.99      | "13.99%"
    }
}
