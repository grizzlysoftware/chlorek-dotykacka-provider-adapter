package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in


import pl.grizzlysoftware.dotykacka.client.v1.api.dto.sales.Shift
import spock.lang.Specification

import static java.lang.System.currentTimeMillis
import static pl.grizzlysoftware.commons.TimeUtils.fromMillis

class DotykackaShiftToCanonicalShiftMapperTest extends Specification {
    def "returns null for given null input" () {
        given:
            def m = new DotykackaShiftToCanonicalShiftMapper()
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    def "maps properly"() {
        given:
            def input = new Shift()
            input.startTime = currentTimeMillis() - 90000
            input.endTime =  currentTimeMillis()
            input.employeeName = "employee 1"
            def m = new DotykackaShiftToCanonicalShiftMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.employeeName == input.employeeName
            output.startTime == fromMillis(input.startTime)
            output.endTime == fromMillis(input.endTime)
    }
}
