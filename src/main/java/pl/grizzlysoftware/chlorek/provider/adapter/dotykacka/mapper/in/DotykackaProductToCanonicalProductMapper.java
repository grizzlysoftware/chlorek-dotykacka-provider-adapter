package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import lombok.extern.slf4j.Slf4j;
import pl.grizzlysoftware.chlorek.core.model.Category;
import pl.grizzlysoftware.chlorek.core.model.Product;
import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider;
import pl.grizzlysoftware.chlorek.core.resolver.ChlorekCsvTagParser;
import pl.grizzlysoftware.chlorek.core.resolver.ContainerTypeTagValueResolver;
import pl.grizzlysoftware.chlorek.core.resolver.TagParser;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static pl.grizzlysoftware.commons.NumberUtils.with2Scale;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
@Slf4j
public class DotykackaProductToCanonicalProductMapper implements Function<pl.grizzlysoftware.dotykacka.client.v2.model.Product, Product> {
    private CategoryProvider categoryProvider;
    private DotykackaMarginToFlatMarginMapper toFlatMarginMapper;
    private DotykackaMarginToMarginMapper toMarginMapper;
    private DotykackaVatToVatMapper toVatMapper;
    private TagParser tagParser;
    private ContainerTypeTagValueResolver containerTypeTagValueResolver;

    public DotykackaProductToCanonicalProductMapper(CategoryProvider categoryProvider) {
        this.categoryProvider = requireNonNull(categoryProvider);
        this.toFlatMarginMapper = new DotykackaMarginToFlatMarginMapper();
        this.toMarginMapper = new DotykackaMarginToMarginMapper();
        this.toVatMapper = new DotykackaVatToVatMapper();
        this.tagParser = new ChlorekCsvTagParser();
        this.containerTypeTagValueResolver = new ContainerTypeTagValueResolver();
    }

    @Override
    public Product apply(pl.grizzlysoftware.dotykacka.client.v2.model.Product in) {
        if (in == null) {
            return null;
        }
        try {
            var out = new Product();
            out.id = in.id;
            out.categoryId = in.categoryId;
            out.categoryName = ofNullable(category(in.categoryId))
                    .map(e -> e.name)
                    .orElse("");
            out.etag = in.etag;
            out.name = in.name;
            out.points = in.points;
            out.currency = in.currency;
            out.ean = String.join(",", ofNullable(in.eans).orElse(emptyList()));
            out.grossSellPrice = with2Scale(in.priceWithVat);
            out.netSellPrice = with2Scale(in.priceWithoutVat);
            out.vat = toVatMapper.apply(in.vat);
            out.minMargin = in.marginMin == null ? BigDecimal.ZERO : BigDecimal.valueOf(in.marginMin);
            out.getTags().addAll(Optional.ofNullable(in.tags)
                    .orElse(emptyList())
                    .stream()
                    .map(tagParser)
                    .collect(toList()));
            out.containerType = containerTypeTagValueResolver.resolve(out);
            out.margin = toMarginMapper.apply(in.margin);
            out.flatMargin = toFlatMarginMapper.apply(in.margin);
            out.isDiscountAllowed = in.isDiscountable != null && in.isDiscountable;
            return out;
        } catch (Throwable e) {
            log.info("Exception while mapping Product: id={}, vat={}, name={}, tags={}", in.id, in.vat, in.name, in.tags);
            throw e;
        }
    }

    private Category category(Long id) {
        try {
            return categoryProvider.getCategory(id);
        } catch (Exception e) {
            return null;
        }
    }
}
