package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out;

import pl.grizzlysoftware.chlorek.core.model.ProductIngredient;
import pl.grizzlysoftware.dotykacka.client.v2.model.Unit;

import java.util.function.Function;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class CanonicalProductIngredientToDotykackaProductIngredientMapper implements Function<ProductIngredient, pl.grizzlysoftware.dotykacka.client.v2.model.ProductIngredient> {
    @Override
    public pl.grizzlysoftware.dotykacka.client.v2.model.ProductIngredient apply(ProductIngredient in) {
        if (in == null) {
            return null;
        }

        var out = new pl.grizzlysoftware.dotykacka.client.v2.model.ProductIngredient();
        out.ingredientId = in.id;
        out.etag = in.etag;
        out.productId = in.productId;
        out.quantity = in.quantity;
        out.unit = Unit.valueOf(in.unit);
        return out;
    }
}
