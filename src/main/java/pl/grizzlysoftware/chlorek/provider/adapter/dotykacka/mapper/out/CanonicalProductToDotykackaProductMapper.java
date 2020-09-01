package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.Product;
import pl.grizzlysoftware.chlorek.core.resolver.ChlorekCsvTagWriter;
import pl.grizzlysoftware.chlorek.core.resolver.TagWriter;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util.TagsToStringTagsMapper;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 *
 * 0 is not the same as NULL
 */
public class CanonicalProductToDotykackaProductMapper implements Function<Product, pl.grizzlysoftware.dotykacka.client.v1.api.dto.product.Product> {
    private TagsToStringTagsMapper toStringTagsMapper;
    private CanonicalVatToDotykackaVatMapper toDotykackaVatMapper;
    private CanonicalMarginsToDotykackaMarginMapper toDotykackaMarginMapper;
    private TagWriter tagWriter;

    public CanonicalProductToDotykackaProductMapper() {
        toStringTagsMapper = new TagsToStringTagsMapper();
        toDotykackaMarginMapper = new CanonicalMarginsToDotykackaMarginMapper();
        toDotykackaVatMapper = new CanonicalVatToDotykackaVatMapper();
        tagWriter = new ChlorekCsvTagWriter();
    }

    @Override
    public pl.grizzlysoftware.dotykacka.client.v1.api.dto.product.Product apply(Product in) {
        if (in == null) {
            return null;
        }
        var out = new pl.grizzlysoftware.dotykacka.client.v1.api.dto.product.Product();
        out.id = in.id;
        out.categoryId = in.categoryId;
        out.name = in.name;
        out.points = in.points == null ? null : in.points.doubleValue();
        out.ean = in.ean;
        out.plu = in.plu;
        out.grossPrice = in.grossSellPrice == null ? -1 : in.grossSellPrice.doubleValue();
        out.netPrice = in.netSellPrice == null ? -1 : in.netSellPrice.doubleValue();
        out.vat = toDotykackaVatMapper.apply(in.vat);
        out.tagsList = toStringTagsMapper.apply(in.getTags().stream().map(tagWriter).collect(toList()));
        out.marginMin = in.minMargin == null ? null : in.minMargin.intValue();
        out.margin = toDotykackaMarginMapper.apply(in.margin, in.flatMargin);

        return out;
    }
}
