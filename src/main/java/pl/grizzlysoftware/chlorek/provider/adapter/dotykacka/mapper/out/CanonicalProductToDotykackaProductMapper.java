package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.Product;
import pl.grizzlysoftware.chlorek.core.resolver.ChlorekCsvTagWriter;
import pl.grizzlysoftware.chlorek.core.resolver.TagWriter;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.SingleStringTagsToCollectionStringTagsMapper;
import pl.grizzlysoftware.dotykacka.client.v2.model.Unit;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static pl.grizzlysoftware.dotykacka.client.v2.model.Product.StockOverdraft.WARN;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 *
 * 0 is not the same as NULL
 */
public class CanonicalProductToDotykackaProductMapper implements Function<Product, pl.grizzlysoftware.dotykacka.client.v2.model.Product> {
    private SingleStringTagsToCollectionStringTagsMapper toCollectionStringTagsMapper;
    private CanonicalVatToDotykackaVatMapper toDotykackaVatMapper;
    private CanonicalMarginsToDotykackaMarginMapper toDotykackaMarginMapper;
    private TagWriter tagWriter;

    public CanonicalProductToDotykackaProductMapper() {
        toCollectionStringTagsMapper = new SingleStringTagsToCollectionStringTagsMapper();
        toDotykackaMarginMapper = new CanonicalMarginsToDotykackaMarginMapper();
        toDotykackaVatMapper = new CanonicalVatToDotykackaVatMapper();
        tagWriter = new ChlorekCsvTagWriter();
    }

    @Override
    public pl.grizzlysoftware.dotykacka.client.v2.model.Product apply(Product in) {
        if (in == null) {
            return null;
        }
        var out = new pl.grizzlysoftware.dotykacka.client.v2.model.Product();
        out.id = in.id;
        out.categoryId = in.categoryId;
        out.etag = in.etag;
        out.name = in.name;
        out.points = in.points;
        out.currency = in.currency;
        out.eans = toCollectionStringTagsMapper.apply(in.ean);
        out.plus = toCollectionStringTagsMapper.apply(in.plu);
        out.priceWithVat = in.grossSellPrice;
        out.priceWithoutVat = in.netSellPrice;
        out.vat = toDotykackaVatMapper.apply(in.vat);
        out.tags = in.getTags().stream().map(tagWriter).collect(toList());
        out.marginMin = in.minMargin == null ? null : in.minMargin.doubleValue();
        out.margin = toDotykackaMarginMapper.apply(in.margin, in.flatMargin);
        out.isDiscountable = in.isDiscountAllowed;

        //hardcoded
        out.unit = Unit.Piece;
        out.stockOverdraft = WARN;
        out.packaging = 1.0;

        return out;
    }
}
