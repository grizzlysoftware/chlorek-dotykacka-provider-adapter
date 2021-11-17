package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.Stockup;
import pl.grizzlysoftware.dotykacka.client.v2.model.WarehouseStockUp;

import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalStockupToDotykackaProductStockupMapper implements Function<Stockup, WarehouseStockUp> {

    private CanonicalInvoiceItemToDotykackaProductStockupItemMapper mapper;

    public CanonicalStockupToDotykackaProductStockupMapper() {
        this.mapper = new CanonicalInvoiceItemToDotykackaProductStockupItemMapper();
    }

    CanonicalStockupToDotykackaProductStockupMapper(CanonicalInvoiceItemToDotykackaProductStockupItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public WarehouseStockUp apply(Stockup in) {
        if (in == null) {
            return null;
        }

        var out = new WarehouseStockUp();
        out.updateProductPurchasePrice = true;
        out.warehouseId = in.warehouseId;
        out.invoiceNumber = in.invoice.number;
        out.note = in.notes;
        out.supplierId = in.invoice.supplierId;
        out.items = ofNullable(in.invoice.items)
                .orElse(emptyList())
                .stream()
                .map(mapper)
                .collect(toList());
        return out;
    }
}
