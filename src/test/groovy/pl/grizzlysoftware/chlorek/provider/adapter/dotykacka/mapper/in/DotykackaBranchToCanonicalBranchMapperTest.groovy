package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.branch.Branch;
import spock.lang.Specification;

class DotykackaBranchToCanonicalBranchMapperTest extends Specification {
    def "returns null for given null input" () {
        given:
            def m = new DotykackaBranchToCanonicalBranchMapper()
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    def "maps properly"() {
        given:
            def input = new Branch()
            input.id = 10L
            input.cloudId = 15L
            input.name = "branch 1"
            def m = new DotykackaBranchToCanonicalBranchMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.id == input.id
            output.cloudId == input.cloudId
            output.name == input.name
    }
}
