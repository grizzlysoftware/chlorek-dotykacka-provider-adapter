package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.InvoicePosition;
import pl.grizzlysoftware.chlorek.core.service.SalesService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaReceiptItemToCanonicalInvoicePositionMapper;
import pl.grizzlysoftware.dotykacka.client.v1.facade.SalesServiceFacade;

import java.time.LocalDateTime;
import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaSalesService implements SalesService {

    private SalesServiceFacade salesService;
    private DotykackaReceiptItemToCanonicalInvoicePositionMapper toCanonicalInvoicePositionMapper;

    public DotykackaSalesService(SalesServiceFacade service) {
        this.salesService = requireNonNull(service);
        this.toCanonicalInvoicePositionMapper = new DotykackaReceiptItemToCanonicalInvoicePositionMapper();
    }

    @Override
    public Collection<InvoicePosition> getInvoicePositions(LocalDateTime startDate, LocalDateTime endDate, int limit, int offset, String sort) {
        var out = salesService.getReceiptItems(startDate, endDate, limit, offset, sort)
                .stream()
                .map(toCanonicalInvoicePositionMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<InvoicePosition> getInvoicePositions(LocalDateTime startDate, LocalDateTime endDate) {
        var out = salesService.getReceiptItems(startDate, endDate)
                .stream()
                .map(toCanonicalInvoicePositionMapper)
                .collect(toList());
        return out;
    }
}
