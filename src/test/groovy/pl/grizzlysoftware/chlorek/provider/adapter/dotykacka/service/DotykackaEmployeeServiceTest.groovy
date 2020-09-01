package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import org.apache.commons.lang3.StringUtils
import pl.grizzlysoftware.chlorek.core.model.Employee
import pl.grizzlysoftware.chlorek.core.model.Shift
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaEmployeeToCanonicalEmployeeMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaShiftToCanonicalShiftMapper
import pl.grizzlysoftware.dotykacka.client.v1.facade.EmployeeServiceFacade
import pl.grizzlysoftware.dotykacka.client.v1.facade.SalesServiceFacade
import spock.lang.Specification

import static java.util.stream.Collectors.toList
import static pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.DotykackaEmployeeTestUtils.employee
import static pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.DotykackaEmployeeTestUtils.shift

class DotykackaEmployeeServiceTest extends Specification {
    def "throws exception when given args are null"(employeeService, salesService) {
        when:
            new DotykackaEmployeeService(employeeService, salesService)
        then:
            thrown(NullPointerException)
        where:
            employeeService             | salesService
            null                        | Mock(SalesServiceFacade)
            Mock(EmployeeServiceFacade) | null
            null                        | null
    }

    def "returns employees"() {
        given:
            def service = Mock(EmployeeServiceFacade) {
                getAllEmployees() >> [employee(null), employee(null), employee(null), employee(null)]
            }
            def m = new DotykackaEmployeeService(service, Mock(SalesServiceFacade))
        when:
            def output = m.getEmployees()
        then:
            output != null
            output.size() == 4
    }

    def "returns shifts"() {
        given:
            def service = Mock(SalesServiceFacade) {
                getShiftRanges(_, _, _) >> [shift(), shift(), shift(), shift()]
            }
            def m = new DotykackaEmployeeService(Mock(EmployeeServiceFacade), service)
        when:
            def output = m.getShifts(null, 0, 0)
        then:
            output != null
            output.size() == 4
    }

    def "invokes toCanonicalShiftMapper for each fetched shift"() {
        given:
            def service = Mock(SalesServiceFacade) {
                getShiftRanges(_, _, _) >> [shift(), shift(), shift(), shift()]
            }
            def m = new DotykackaEmployeeService(Mock(EmployeeServiceFacade), service)
        when:
            m.toCanonicalShiftMapper = Mock(DotykackaShiftToCanonicalShiftMapper) {
                4 * apply(_) >> new Shift()
            }
        then:
            def output = m.getShifts(null, 0, 0)
    }

    def "invokes toCanonicalEmployeeMapper for each fetched employee"() {
        given:
            def service = Mock(EmployeeServiceFacade) {
                getAllEmployees() >> [employee(null), employee(null), employee(null), employee(null)]
            }
            def m = new DotykackaEmployeeService(service, Mock(SalesServiceFacade))
        when:
            m.toCanonicalEmployeeMapper = Mock(DotykackaEmployeeToCanonicalEmployeeMapper) {
                4 * apply(_) >> new Employee()
            }
        then:
            def output = m.getEmployees()
    }

    def "returned employees are sorted alphabetically ascending"() {
        given:
            def service = Mock(EmployeeServiceFacade) {
                getAllEmployees() >> [employee("b"), employee("c"), employee("a"), employee("e")]
            }
            def m = new DotykackaEmployeeService(service, Mock(SalesServiceFacade))
        when:
            def output = m.getEmployees()
            def sorted = output.stream()
                    .sorted({ e1, e2 -> StringUtils.compare(e1.name, e2.name) })
                    .collect(toList())
        then:
            output == sorted
    }

    def "does not throw exception if returned employee contents are empty"() {
        given:
            def service = Mock(EmployeeServiceFacade) {
                getAllEmployees() >> [employee(null), employee(null), employee(null), employee(null)]
            }
            def m = new DotykackaEmployeeService(service, Mock(SalesServiceFacade))
            m.toCanonicalEmployeeMapper = Mock(DotykackaEmployeeToCanonicalEmployeeMapper) {
                apply(_) >> new Employee()
            }
        when:
            def output = m.getEmployees()
        then:
            output != null
            output.size() == 4
            noExceptionThrown()
    }
}
