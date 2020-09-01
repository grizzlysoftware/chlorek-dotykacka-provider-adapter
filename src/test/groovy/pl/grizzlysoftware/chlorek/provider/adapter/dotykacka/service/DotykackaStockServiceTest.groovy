package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import spock.lang.Specification;

class DotykackaStockServiceTest extends Specification {
    def "throws exception when given args are null"() {
        when:
            new DotykackaStockService(null)
        then:
            thrown(NullPointerException)
    }
}
