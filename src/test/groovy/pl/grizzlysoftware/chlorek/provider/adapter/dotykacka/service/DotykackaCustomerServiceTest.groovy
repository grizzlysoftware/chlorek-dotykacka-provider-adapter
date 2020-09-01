package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service


import spock.lang.Specification

class DotykackaCustomerServiceTest extends Specification {
    def "throws exception when given args are null"() {
        when:
            new DotykackaCustomerService(null)
        then:
            thrown(NullPointerException)
    }
}
