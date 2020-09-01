package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.Employee;
import pl.grizzlysoftware.chlorek.core.model.Shift;
import pl.grizzlysoftware.chlorek.core.service.EmployeeService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaEmployeeToCanonicalEmployeeMapper;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaShiftToCanonicalShiftMapper;
import pl.grizzlysoftware.dotykacka.client.v1.facade.EmployeeServiceFacade;
import pl.grizzlysoftware.dotykacka.client.v1.facade.SalesServiceFacade;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static pl.grizzlysoftware.commons.ComparatorUtils.stringComparator;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaEmployeeService implements EmployeeService {

    private EmployeeServiceFacade employeeService;
    private SalesServiceFacade salesService;
    private DotykackaEmployeeToCanonicalEmployeeMapper toCanonicalEmployeeMapper;
    private DotykackaShiftToCanonicalShiftMapper toCanonicalShiftMapper;

    public DotykackaEmployeeService(EmployeeServiceFacade service, SalesServiceFacade salesService) {
        this.employeeService = requireNonNull(service);
        this.salesService = requireNonNull(salesService);
        this.toCanonicalEmployeeMapper = new DotykackaEmployeeToCanonicalEmployeeMapper();
        this.toCanonicalShiftMapper = new DotykackaShiftToCanonicalShiftMapper();
    }

    @Override
    public Collection<Employee> getEmployees() {
        var out = employeeService.getAllEmployees()
                .stream()
                .map(toCanonicalEmployeeMapper)
                .sorted(stringComparator(e -> e.name))
                .collect(toList());
        return out;
    }

    @Override
    public Collection<Shift> getShifts(Long branchId, int limit, int offset) {
        var out = salesService.getShiftRanges(branchId, limit, offset)
                .stream()
                .map(toCanonicalShiftMapper)
                .collect(toList());
        return out;
    }
}
