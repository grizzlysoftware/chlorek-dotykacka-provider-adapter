package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import org.apache.commons.lang3.StringUtils
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaCategoryToCanonicalCategoryMapper
import pl.grizzlysoftware.dotykacka.client.v2.facade.CategoryServiceFacade
import spock.lang.Specification

import static java.util.stream.Collectors.toList
import static pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.DotykackaCategoryTestUtils.category

class DotykackaCategoryServiceTest extends Specification {
    def "throws exception when given args are null"() {
        when:
            new DotykackaCategoryService(null)
        then:
            thrown(NullPointerException)
    }

    def "returns categories"() {
        given:
            def service = Mock(CategoryServiceFacade) {
                getAllCategories() >> [category(""), category(""), category(""), category("")]
            }
            def m = new DotykackaCategoryService(service)
        when:
            def output = m.getCategories()
        then:
            output != null
            output.size() == 4
    }

    def "returned categories are sorted alphabetically ascending"() {
        given:
            def service = Mock(CategoryServiceFacade) {
                getAllCategories() >> [category("b"), category("c"), category("a"), category("e")]
            }
            def m = new DotykackaCategoryService(service)
        when:
            def output = m.getCategories()
            def sorted = output.stream()
                    .sorted({ e1, e2 -> StringUtils.compare(e1.name, e2.name) })
                    .collect(toList())
        then:
            output == sorted
    }

    def "invokes toCanonicalCategoryMapper for each fetched category"() {
        given:
            def service = Mock(CategoryServiceFacade) {
                getAllCategories() >> [category(""), category(""), category(""), category("")]
            }
            def m = new DotykackaCategoryService(service)
            m.toCanonicalCategoryMapper = Mock(DotykackaCategoryToCanonicalCategoryMapper) {
                4 * apply(_) >> {
                    def out = new pl.grizzlysoftware.chlorek.core.model.Category()
                    out.name = ""
                    return out
                }
            }
            def output = m.getCategories()
    }

    def "does not throw exception if returned category contents are empty"() {
        given:
            def service = Mock(CategoryServiceFacade) {
                getAllCategories() >> [category(null), category(null), category(null), category(null)]
            }
            def m = new DotykackaCategoryService(service)
            m.toCanonicalCategoryMapper = Mock(DotykackaCategoryToCanonicalCategoryMapper) {
                apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Category()
            }
        when:
            def output = m.getCategories()
        then:
            output != null
            output.size() == 4
            noExceptionThrown()
    }

}
