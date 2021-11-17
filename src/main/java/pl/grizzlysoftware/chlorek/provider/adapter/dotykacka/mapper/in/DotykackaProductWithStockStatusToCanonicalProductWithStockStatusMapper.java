package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider;
import pl.grizzlysoftware.chlorek.core.resolver.ChlorekCsvTagParser;
import pl.grizzlysoftware.chlorek.core.resolver.TagParser;
import pl.grizzlysoftware.dotykacka.client.v2.model.ProductStock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static pl.grizzlysoftware.commons.NumberUtils.with2Scale;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper implements Function<ProductStock, pl.grizzlysoftware.chlorek.core.model.ProductWithStockStatus> {
    private CategoryProvider categoryProvider;
    private DotykackaMarginToMarginMapper toMarginMapper;
    private DotykackaMarginToFlatMarginMapper toFlatMarginMapper;
    private DotykackaVatToVatMapper toVatMapper;
    private TagParser tagParser;

    public DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper(CategoryProvider categoryProvider) {
        this.categoryProvider = requireNonNull(categoryProvider);
        this.toMarginMapper = new DotykackaMarginToMarginMapper();
        this.toFlatMarginMapper = new DotykackaMarginToFlatMarginMapper();
        this.toVatMapper = new DotykackaVatToVatMapper();
        this.tagParser = new ChlorekCsvTagParser();
    }

    @Override
    public pl.grizzlysoftware.chlorek.core.model.ProductWithStockStatus apply(ProductStock in) {
        if (in == null) {
            return null;
        }
        var out = new pl.grizzlysoftware.chlorek.core.model.ProductWithStockStatus();
        out.id = in.id;
        out.categoryId = in.categoryId;
        out.categoryName = ofNullable(categoryProvider.getCategory(in.categoryId))
                .map(e -> e.name)
                .orElse("");
        out.name = in.name;
        out.etag = in.etag;
        out.points = in.points;
        out.ean = String.join(",", ofNullable(in.eans)
                .orElse(emptyList()));
        out.plu = String.join(",", ofNullable(in.plus)
                .orElse(emptyList()));
        out.grossSellPrice = with2Scale(in.priceWithVat);
        out.netSellPrice = with2Scale(in.priceWithoutVat);
        out.vat = toVatMapper.apply(in.vat);
        out.getTags()
                .addAll(Optional.ofNullable(in.tags)
                        .orElse(emptyList())
                        .stream()
                        .map(tagParser)
                        .collect(toSet())
                );
        out.minMargin = in.marginMin == null ? BigDecimal.ZERO : BigDecimal.valueOf(in.marginMin);
        out.isDeleted = in.isDeleted != null && in.isDeleted;
        out.margin = toMarginMapper.apply(in.margin);
        out.flatMargin = toFlatMarginMapper.apply(in.margin);
//        out.lastInventoryValue = in.lastInventoryValue;
        out.lastPurchaseNetPrice = with2Scale(in.purchasePriceWithoutVat);
        out.stockQuantity = in.quantity;
        out.isDiscountAllowed = in.isDiscountable != null && in.isDiscountable;
        //out.warehouseId it is not applied here as it is hard to determine which warehouse it belongs to
        //TODO however this mapper is used to fetch product with stock status for GIVEN warehouseId so it should contain just 1 warehouse in it - check that out

        return out;
    }
}
