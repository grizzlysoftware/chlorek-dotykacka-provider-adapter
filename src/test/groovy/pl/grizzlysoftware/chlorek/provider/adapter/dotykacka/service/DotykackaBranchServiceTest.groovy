package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaBranchToCanonicalBranchMapper
import pl.grizzlysoftware.dotykacka.client.v2.model.Branch
import pl.grizzlysoftware.dotykacka.client.v2.facade.BranchServiceFacade
import spock.lang.Specification

class DotykackaBranchServiceTest extends Specification {
    def "throws exception when given args are null"() {
        when:
            new DotykackaBranchService(null)
        then:
            thrown(NullPointerException)
    }

    def "returns branches"() {
        given:
            def service = Mock(BranchServiceFacade) {
                getAllBranches() >> [Mock(Branch), Mock(Branch), Mock(Branch), Mock(Branch)]
            }
            def m = new DotykackaBranchService(service)
        when:
            def output = m.getBranches()
        then:
            output != null
            output.size() == 4
    }

    def "invokes toCanonicalBranchMapper for each fetched branch"() {
        given:
            def service = Mock(BranchServiceFacade) {
                getAllBranches() >> [Mock(Branch), Mock(Branch), Mock(Branch), Mock(Branch)]
            }
            def m = new DotykackaBranchService(service)
            m.toCanonicalBranchMapper = Mock(DotykackaBranchToCanonicalBranchMapper)
        when:
            def output = m.getBranches()
        then:
            4 * m.toCanonicalBranchMapper.apply(_)
    }
}
