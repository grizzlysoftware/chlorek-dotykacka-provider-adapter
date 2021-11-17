package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import lombok.extern.slf4j.Slf4j;
import pl.grizzlysoftware.chlorek.core.model.Employee;
import pl.grizzlysoftware.chlorek.core.model.Shift;
import pl.grizzlysoftware.chlorek.core.service.EmployeeService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaEmployeeToCanonicalEmployeeMapper;
import pl.grizzlysoftware.dotykacka.client.v2.facade.EmployeeServiceFacade;
import pl.grizzlysoftware.dotykacka.client.v2.facade.MoneyLogServiceFacade;
import pl.grizzlysoftware.dotykacka.client.v2.model.MoneyLog;
import pl.grizzlysoftware.dotykacka.client.v2.model.TransactionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static pl.grizzlysoftware.commons.ComparatorUtils.stringComparator;
import static pl.grizzlysoftware.dotykacka.client.v2.model.TransactionType.REGISTER_CLOSE;
import static pl.grizzlysoftware.dotykacka.client.v2.model.TransactionType.REGISTER_OPEN;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
@Slf4j
public class DotykackaEmployeeService implements EmployeeService {
    private EmployeeServiceFacade employeeService;
    private MoneyLogServiceFacade moneyLogServiceFacade;
    private DotykackaEmployeeToCanonicalEmployeeMapper toCanonicalEmployeeMapper;
    private Map<Long, pl.grizzlysoftware.dotykacka.client.v2.model.Employee> employeeCache;

    public DotykackaEmployeeService(EmployeeServiceFacade service, MoneyLogServiceFacade moneyLogServiceFacade) {
        this.employeeService = requireNonNull(service);
        this.moneyLogServiceFacade = requireNonNull(moneyLogServiceFacade);
        this.toCanonicalEmployeeMapper = new DotykackaEmployeeToCanonicalEmployeeMapper();
        this.employeeCache = new HashMap<>();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        var dotykackaIn = employeeCache.computeIfAbsent(id, employeeService::getEmployee);
        var out = toCanonicalEmployeeMapper.apply(dotykackaIn);
        return out;
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
    public Collection<Shift> getShifts(Long branchId, int page, int pageSize) {
        var filter = String.format("_branchId|eq|%s;transactionType|in|%s,%s", branchId, REGISTER_CLOSE, REGISTER_OPEN);
        final var data = moneyLogServiceFacade.getMoneyLogs(page + 1, pageSize, filter, "-created").data;
        final var out = new ArrayList<Shift>(data.size() / 2);
        Shift shift = new Shift();
        for (MoneyLog in : data) {
            if (REGISTER_OPEN == in.transactionType) {
                shift.startTime = in.createdAt.toLocalDateTime();
                out.add(shift);
            } else if (REGISTER_CLOSE == in.transactionType) {
                shift.endTime = in.createdAt.toLocalDateTime();
                shift.employeeName = getEmployeeById(in.employeeId).name;
                shift = new Shift();
            }
            shift.branchId = in.branchId;
        }

        if (isNull(shift.endTime) && !isNull(shift.startTime)) {
            //TODO
            shift.endTime = shift.startTime.plusHours(12);
        }

        return out;
    }
}
