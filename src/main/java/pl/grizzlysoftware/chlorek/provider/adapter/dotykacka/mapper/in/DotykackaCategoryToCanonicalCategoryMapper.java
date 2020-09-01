package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in;

import pl.grizzlysoftware.dotykacka.client.v1.api.dto.category.Category;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaCategoryToCanonicalCategoryMapper implements Function<Category, pl.grizzlysoftware.chlorek.core.model.Category> {
    private DotykackaVatToVatMapper vatMapper;

    public DotykackaCategoryToCanonicalCategoryMapper() {
        vatMapper = new DotykackaVatToVatMapper();
    }

    @Override
    public pl.grizzlysoftware.chlorek.core.model.Category apply(Category in) {
        if (in == null) {
            return null;
        }

        var out = new pl.grizzlysoftware.chlorek.core.model.Category();
        out.id = in.id;
        out.cloudId = in.cloudId == null ? null : in.cloudId.intValue();
        out.name = in.name;
        out.vat = vatMapper.apply(in.vat);
        out.isFiscalizationEnabled = in.fiscalizationDisabled == null || in.fiscalizationDisabled == 0;
        out.isDisplayed = in.isDisplayed == null ? false : in.isDisplayed;
        out.isDeleted = in.isDeleted == null ? false : in.isDeleted;
        return out;
    }
}
