package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.InvoiceItem;
import pl.grizzlysoftware.dotykacka.client.v2.model.StockUpItem;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalInvoiceItemToDotykackaProductStockupItemMapper implements Function<InvoiceItem, StockUpItem> {
    @Override
    public StockUpItem apply(InvoiceItem in) {
        if (in == null) {
            return null;
        }
        var out = new StockUpItem();
        out.productId = in.itemId;
        out.sellPrice = in.grossSellPrice;
        out.purchasePrice = in.netPurchasePrice;
        out.quantity = in.quantity;
        return out;
    }
}
