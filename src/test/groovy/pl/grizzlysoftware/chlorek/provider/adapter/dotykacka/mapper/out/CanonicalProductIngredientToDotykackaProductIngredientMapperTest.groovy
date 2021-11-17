package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out

import pl.grizzlysoftware.chlorek.core.model.ProductIngredient
import pl.grizzlysoftware.dotykacka.client.v2.model.Unit
import spock.lang.Specification

class CanonicalProductIngredientToDotykackaProductIngredientMapperTest extends Specification {
    def "returns null for given null input"() {
        given:
            def m = new CanonicalProductIngredientToDotykackaProductIngredientMapper()
        when:
            def out = m.apply(null)
        then:
            out == null
    }

    def "maps properly"() {
        given:
            def input = new ProductIngredient()
            input.id = 10L
            input.productId = 15L
            input.quantity = 55
            input.unit = Unit.Deciliter.toString()
            input.etag = UUID.randomUUID().toString()
            def m = new CanonicalProductIngredientToDotykackaProductIngredientMapper()
        when:
            def output = m.apply(input)
        then:
            output != null
            output.ingredientId == input.id
            output.productId == input.productId
            output.quantity == input.quantity
            output.unit == Unit.valueOf(input.unit)
            output.etag == input.etag
    }
}
