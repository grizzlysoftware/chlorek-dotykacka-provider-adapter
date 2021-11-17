package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in

import pl.grizzlysoftware.dotykacka.client.v2.model.Category;
import spock.lang.Specification;

class DotykackaCategoryToCanonicalCategoryMapperTest extends Specification {
    def "returns null for given null input"() {
        given:
            def m = new DotykackaCategoryToCanonicalCategoryMapper()
        when:
            def output = m.apply(null)
        then:
            output == null
    }

    def "maps properly"(isDisplayed, eIsDisplayed, isDeleted, eIsDeleted) {
        given:
            def input = new Category()
            input.id = 10L
            input.cloudId = 15L
            input.name = "category 1"
            input.vat = 23.9
            input.isDisplayed = isDisplayed
            input.isDeleted = isDeleted
            input.etag = UUID.randomUUID().toString()
            def m = new DotykackaCategoryToCanonicalCategoryMapper()
            m.vatMapper = Mock(DotykackaVatToVatMapper)
        when:
            def output = m.apply(input)
        then:
            output != null
            output.id == input.id
            output.cloudId == input.cloudId
            output.etag == input.etag
            output.name == input.name
            output.isDisplayed == eIsDisplayed
            output.isDeleted == eIsDeleted

            1 * m.vatMapper.apply(_)
        where:
            isDisplayed || eIsDisplayed | isDeleted || eIsDeleted
            null        || false        | null      || false
            false       || false        | false     || false
            true        || true         | true      || true
    }
}
