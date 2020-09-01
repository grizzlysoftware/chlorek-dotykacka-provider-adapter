package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.InvoiceItem;
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.ProductStockupItem;

import java.util.function.Function;

import static pl.grizzlysoftware.commons.NumberUtils.safeDouble;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalInvoiceItemToDotykackaProductStockupItemMapper implements Function<InvoiceItem, ProductStockupItem> {
    @Override
    public ProductStockupItem apply(InvoiceItem in) {
        if (in == null) {
            return null;
        }
        var out = new ProductStockupItem();
        out.productId = in.itemId;
        out.grossSellPrice = safeDouble(in.grossSellPrice);
        out.netPurchasePrice = safeDouble(in.netPurchasePrice);
        out.quantity = in.quantity;
        return out;
    }
}
