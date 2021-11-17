package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import org.apache.commons.lang3.StringUtils
import pl.grizzlysoftware.chlorek.core.model.Employee
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaEmployeeToCanonicalEmployeeMapper
import pl.grizzlysoftware.dotykacka.client.v2.facade.EmployeeServiceFacade
import pl.grizzlysoftware.dotykacka.client.v2.facade.MoneyLogServiceFacade
import pl.grizzlysoftware.dotykacka.client.v2.model.ResultPage
import spock.lang.Specification

import static java.time.ZonedDateTime.now
import static java.util.stream.Collectors.toList
import static pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.DotykackaEmployeeMoneyLogTestUtils.moneyLog
import static pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.DotykackaEmployeeTestUtils.employee
import static pl.grizzlysoftware.dotykacka.client.v2.model.TransactionType.REGISTER_CLOSE
import static pl.grizzlysoftware.dotykacka.client.v2.model.TransactionType.REGISTER_OPEN

class DotykackaEmployeeServiceTest extends Specification {
    EmployeeServiceFacade employeeService
    MoneyLogServiceFacade moneyLogService
    DotykackaEmployeeService service

    def setup() {
        def moneyLogs = [moneyLog(REGISTER_OPEN, now()),
                         moneyLog(REGISTER_CLOSE, now()),
                         moneyLog(REGISTER_OPEN, now()),
                         moneyLog(REGISTER_CLOSE, now())
        ]
        def result = new ResultPage()
        result.data = moneyLogs
        moneyLogService = Mock(MoneyLogServiceFacade) {
            getMoneyLogs(_, _, _, _) >> result
        }
        employeeService = Mock(EmployeeServiceFacade) {
            getAllEmployees() >> [employee("b"), employee("c"), employee("a"), employee("e")]
            getEmployee(_) >> employee("b")
        }

        service = new DotykackaEmployeeService(employeeService, moneyLogService)
    }

    def "throws exception when given args are null"(employeeService, salesService) {
        when:
            new DotykackaEmployeeService(employeeService, salesService)
        then:
            thrown(NullPointerException)
        where:
            employeeService             | salesService
            null                        | Mock(MoneyLogServiceFacade)
            Mock(EmployeeServiceFacade) | null
            null                        | null
    }

    def "returns employees"() {
        when:
            def output = service.getEmployees()
        then:
            output != null
            output.size() == 4
    }

    def "returns shifts"() {
        when:
            def output = service.getShifts(null, 0, 0)
        then:
            output != null
            output.size() == 2
    }

    def "invokes toCanonicalEmployeeMapper for each fetched employee"() {
        when:
            service.toCanonicalEmployeeMapper = Mock(DotykackaEmployeeToCanonicalEmployeeMapper) {
                4 * apply(_) >> new Employee()
            }
        then:
            def output = service.getEmployees()
    }

    def "returned employees are sorted alphabetically ascending"() {
        when:
            def output = service.getEmployees()
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
            def m = new DotykackaEmployeeService(service, moneyLogService)
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
