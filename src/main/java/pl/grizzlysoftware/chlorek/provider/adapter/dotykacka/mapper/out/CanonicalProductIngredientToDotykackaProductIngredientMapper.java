package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.ProductIngredient;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalProductIngredientToDotykackaProductIngredientMapper implements Function<ProductIngredient, pl.grizzlysoftware.dotykacka.client.v1.api.dto.ProductIngredient> {
    @Override
    public pl.grizzlysoftware.dotykacka.client.v1.api.dto.ProductIngredient apply(ProductIngredient in) {
        if (in == null) {
            return null;
        }

        var out = new pl.grizzlysoftware.dotykacka.client.v1.api.dto.ProductIngredient();
        out.ingredientId = in.id;
        out.productId = in.productId;
        out.quantity = in.quantity;
        out.unit = in.unit;
        return out;
    }
}
