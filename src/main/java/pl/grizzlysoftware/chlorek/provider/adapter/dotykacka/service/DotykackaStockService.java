package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.Stockup;
import pl.grizzlysoftware.chlorek.core.service.StockService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalStockupToDotykackaProductStockupMapper;
import pl.grizzlysoftware.dotykacka.client.v2.facade.WarehouseServiceFacade;

import static java.util.Objects.requireNonNull;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaStockService implements StockService {

    private final WarehouseServiceFacade service;
    private final CanonicalStockupToDotykackaProductStockupMapper toDotykackaProductStockupMapper;

    public DotykackaStockService(WarehouseServiceFacade service) {
        this.service = requireNonNull(service);
        this.toDotykackaProductStockupMapper = new CanonicalStockupToDotykackaProductStockupMapper();
    }

    @Override
    public void stockup(Stockup in) {
        var dotykackaOut = toDotykackaProductStockupMapper.apply(in);
        service.stockup(dotykackaOut);
    }

    @Override
    public void pendingStockup(Stockup stockup) {
        throw new UnsupportedOperationException("AND SHOULD NOT BE SUPPORTED - at least not now");
    }

    @Override
    public void deletePendingStockupByInvoiceNr(String invoiceNumber) {
        throw new UnsupportedOperationException("AND SHOULD NOT BE SUPPORTED - at least not now");
    }
}
