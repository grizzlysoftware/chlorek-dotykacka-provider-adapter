package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.chlorek.core.model.InvoicePosition;
import pl.grizzlysoftware.dotykacka.client.v2.model.OrderItem;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaOrderItemToCanonicalInvoicePositionMapper implements Function<OrderItem, InvoicePosition> {

    @Override
    public InvoicePosition apply(OrderItem in) {
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
//        out.refundId = in.refundId;
        out.name = in.name;
        out.ean = String.join(",", Optional.ofNullable(in.eans).orElseGet(Collections::emptyList));
        out.tags = in.tags;
        out.cancelledAt = in.canceledAt == null ? null : in.canceledAt.toLocalDateTime();
        out.completedAt = in.completedAt == null ? null : in.completedAt.toLocalDateTime();
        out.quantity = in.quantity;
        out.vat = in.vat == null ? BigDecimal.ZERO : in.vat.subtract(BigDecimal.ONE);
        out.priceWithoutVat = in.totalPriceWithVat;
//        out.priceBilledWithoutVat = in.billedUnitPrice;
        out.priceBilledWithoutVat = in.billedUnitPriceWithoutVat;

        return out;
    }
}
