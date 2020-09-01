package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.chlorek.core.model.InvoicePosition;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.SingleStringTagsToCollectionStringTagsMapper;
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.sales.ReceiptItem;

import java.math.BigDecimal;
import java.util.function.Function;

import static pl.grizzlysoftware.commons.TimeUtils.fromMillis;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaReceiptItemToCanonicalInvoicePositionMapper implements Function<ReceiptItem, InvoicePosition> {
    protected SingleStringTagsToCollectionStringTagsMapper tagsMapper;

    public DotykackaReceiptItemToCanonicalInvoicePositionMapper() {
        this.tagsMapper = new SingleStringTagsToCollectionStringTagsMapper();
    }

    @Override
    public InvoicePosition apply(ReceiptItem in) {
        if (in == null) {
            return null;
        }

        var out = new InvoicePosition();
        out.invoiceId = in.orderId;
        out.branchId = in.branchId;
        out.cloudId = in.cloudId == null ? null : in.cloudId.intValue();
        out.categoryId = in.categoryId;
        out.customerId = in.customerId;
        out.productId = in.productId;
        out.employeeId = in.employeeId;
        out.refundId = in.refundId;
        out.name = in.name;
        out.ean = in.ean;
        out.tags = tagsMapper.apply(in.tagsList);
        out.cancelledAt = fromMillis(in.cancelledAt);
        out.completedAt = fromMillis(in.completedAt);
        out.quantity = in.quantity;
        out.vat = in.vat == null ? BigDecimal.ZERO : in.vat.subtract(BigDecimal.ONE);
        out.priceWithoutVat = in.totalPriceWithVat;
        out.priceBilledWithoutVat = in.billedUnitPrice;

        return out;
    }
}
