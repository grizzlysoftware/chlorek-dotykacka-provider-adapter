package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.Stockup;
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.ProductStockup;

import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalStockupToDotykackaProductStockupMapper implements Function<Stockup, ProductStockup> {

    private CanonicalInvoiceItemToDotykackaProductStockupItemMapper mapper;

    public CanonicalStockupToDotykackaProductStockupMapper() {
        this.mapper = new CanonicalInvoiceItemToDotykackaProductStockupItemMapper();
    }

    @Override
    public ProductStockup apply(Stockup in) {
        if (in == null) {
            return null;
        }

        var out = new ProductStockup();
        out.updateSellPrice = true;
        out.invoiceNumber = in.invoice.number;
        out.note = in.notes;
        out.supplierId = in.invoice.supplierId;
        out.stockupItems = ofNullable(in.invoice.items)
                .orElse(emptyList())
                .stream()
                .map(mapper)
                .collect(toList());
        return out;
    }
}
