package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.InvoicePosition;
import pl.grizzlysoftware.chlorek.core.service.SalesService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaOrderItemToCanonicalInvoicePositionMapper;
import pl.grizzlysoftware.dotykacka.client.v2.facade.OrderItemServiceFacade;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaSalesService implements SalesService {

    private OrderItemServiceFacade orderItemServiceFacade;
    private DotykackaOrderItemToCanonicalInvoicePositionMapper toCanonicalInvoicePositionMapper;

    public DotykackaSalesService(OrderItemServiceFacade service) {
        this.orderItemServiceFacade = requireNonNull(service);
        this.toCanonicalInvoicePositionMapper = new DotykackaOrderItemToCanonicalInvoicePositionMapper();
    }

    DotykackaSalesService(OrderItemServiceFacade orderItemServiceFacade, DotykackaOrderItemToCanonicalInvoicePositionMapper toCanonicalInvoicePositionMapper) {
        this.orderItemServiceFacade = orderItemServiceFacade;
        this.toCanonicalInvoicePositionMapper = toCanonicalInvoicePositionMapper;
    }

    @Override
    public Collection<InvoicePosition> getInvoicePositions(LocalDateTime startDate, LocalDateTime endDate, int page, int pageSize, String sort) {
        var out = orderItemServiceFacade.getOrderItemsForTimeRange(zdt(startDate), zdt(endDate), page + 1, pageSize, sort)
                .data
                .stream()
                .map(toCanonicalInvoicePositionMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<InvoicePosition> getInvoicePositions(LocalDateTime startDate, LocalDateTime endDate) {
        var out = orderItemServiceFacade.getAllOrderItemsForTimeRange(zdt(startDate), zdt(endDate), null)
                .stream()
                .map(toCanonicalInvoicePositionMapper)
                .collect(toList());
        return out;
    }

    private ZonedDateTime zdt(LocalDateTime in) {
        return in == null ? null : ZonedDateTime.ofInstant(in.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
