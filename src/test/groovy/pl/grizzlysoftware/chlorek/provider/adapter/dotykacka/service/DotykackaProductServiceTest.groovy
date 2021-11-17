package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import pl.grizzlysoftware.chlorek.core.model.Category
import pl.grizzlysoftware.chlorek.core.model.ProductIngredient
import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductToCanonicalProductMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductIngredientToDotykackaProductIngredientMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductToDotykackaProductMapper
import pl.grizzlysoftware.dotykacka.client.v2.facade.ProductIngredientServiceFacade
import pl.grizzlysoftware.dotykacka.client.v2.facade.WarehouseServiceFacade
import pl.grizzlysoftware.dotykacka.client.v2.model.Product
import pl.grizzlysoftware.dotykacka.client.v2.model.ProductStock
import pl.grizzlysoftware.dotykacka.client.v2.facade.ProductServiceFacade
import pl.grizzlysoftware.dotykacka.client.v2.model.ResultPage
import pl.grizzlysoftware.dotykacka.client.v2.model.Unit
import spock.lang.Specification

class DotykackaProductServiceTest extends Specification {

    CategoryProvider categoryProvider
    ProductServiceFacade productService
    ProductIngredientServiceFacade productIngredientService
    WarehouseServiceFacade warehouseService

    def setup() {
        def products = [
                Mock(Product), Mock(Product),
                Mock(Product), Mock(Product),
                Mock(Product), Mock(Product)
        ]
        def result = new ResultPage()
        result.data = products
        categoryProvider = Mock(CategoryProvider) {
            getCategory(_) >> new Category()
        }
        productService = Mock(ProductServiceFacade) {
            getProducts(_, _, _) >> result
            getProducts(_, _, _, _, _) >> result
            getAllProducts(_) >> products
        }
        productIngredientService = Mock(ProductIngredientServiceFacade) {

        }
        warehouseService = Mock(WarehouseServiceFacade) {
            getProductStocks(_) >> [
                    Mock(ProductStock), Mock(ProductStock),
                    Mock(ProductStock), Mock(ProductStock),
                    Mock(ProductStock), Mock(ProductStock)
            ]
        }
    }

    def "throws exception when given args are null"(categoryProvider, productService) {
        when:
            new DotykackaProductService(categoryProvider, productService, null, null)
        then:
            thrown(NullPointerException)
        where:
            categoryProvider       | productService
            null                   | Mock(ProductServiceFacade)
            Mock(CategoryProvider) | null
            null                   | null
    }

    def "returns products with stock status #1"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
        when:
            def out = m.getProductsWithStockStatus(0, null)
        then:
            out != null
            out.size() == 6
    }

    def "returns products with stock status #2"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
        when:
            def out = m.getProductsWithStockStatus(0, 0, 0, null)
        then:
            out != null
            out.size() == 6
    }

    def "invokes toCanonicalProductWithStockStatusMapper for each existing ProductStock #1"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
            m.toCanonicalProductWithStockStatusMapper = Mock(DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper)
        when:
            def out = m.getProductsWithStockStatus(0, null)
        then:
            6 * m.toCanonicalProductWithStockStatusMapper.apply(_)
    }

    def "invokes toCanonicalProductWithStockStatusMapper for each existing ProductStock #2"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
            m.toCanonicalProductWithStockStatusMapper = Mock(DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper)
        when:
            def out = m.getProductsWithStockStatus(0, 0, 0, null)
        then:
            6 * m.toCanonicalProductWithStockStatusMapper.apply(_)
    }

    def "returns products #1"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
        when:
            def out = m.getProducts(null)
        then:
            out != null
            out.size() == 6
    }

    def "returns products #2"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
        when:
            def out = m.getProducts(0, 0, null)
        then:
            out != null
            out.size() == 6
    }

    def "invokes toCanonicalProductMapper for each existing Product #1"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
            m.toCanonicalProductMapper = Mock(DotykackaProductToCanonicalProductMapper)
        when:
            def out = m.getProducts(null)
        then:
            6 * m.toCanonicalProductMapper.apply(_)
    }

    def "invokes toCanonicalProductMapper for each existing Product #2"() {
        given:
            def m = new DotykackaProductService(categoryProvider, productService, productIngredientService, warehouseService)
            m.toCanonicalProductMapper = Mock(DotykackaProductToCanonicalProductMapper)
        when:
            def out = m.getProducts(0, 0, null)
        then:
            6 * m.toCanonicalProductMapper.apply(_)
    }

    def "creates product"() {
        given:
            def productService = Mock(ProductServiceFacade) {
                createProduct(_) >> new Product()
            }
            def m = new DotykackaProductService(Mock(CategoryProvider), productService, productIngredientService, warehouseService)
        when:
            def out = m.createProduct(new pl.grizzlysoftware.chlorek.core.model.Product())
        then:
            out != null
    }

    def "invokes toDotykackaProductMapper and toCanonicalProductMapper when creating product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
            m.toDotykackaProductMapper = Mock(CanonicalProductToDotykackaProductMapper)
            m.toCanonicalProductMapper = Mock(DotykackaProductToCanonicalProductMapper)
        when:
            def out = m.createProduct(new pl.grizzlysoftware.chlorek.core.model.Product())
        then:
            1 * m.toDotykackaProductMapper.apply(_)
        then:
            1 * m.productService.createProduct(_) >> new Product()
        then:
            1 * m.toCanonicalProductMapper.apply(_)
    }

    def "throws NullPointerException when given product is null arg while creating product"() {
        given:
            def productService = Mock(ProductServiceFacade) {
                createProduct(_) >> new Product()
            }
            def m = new DotykackaProductService(Mock(CategoryProvider), productService, productIngredientService, warehouseService)
        when:
            def out = m.createProduct(null)
        then:
            thrown(NullPointerException)
    }

    def "deletes product"() {
        given:
            def productService = Mock(ProductServiceFacade) {
                deleteProduct(_) >> new Product()
            }
            def m = new DotykackaProductService(Mock(CategoryProvider), productService, productIngredientService, warehouseService)
        when:
            def out = m.deleteProduct(1L)
        then:
            out != null
    }

    def "invokes productService.deleteProduct and toCanonicalProductMapper while deleting product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
            m.toCanonicalProductMapper = Mock(DotykackaProductToCanonicalProductMapper)
        when:
            def out = m.deleteProduct(1L)
        then:
            out != null
        then:
            1 * m.productService.deleteProduct(_) >> new Product()
        then:
            1 * m.toCanonicalProductMapper.apply(_) >> new pl.grizzlysoftware.chlorek.core.model.Product()
    }

    def "throws NullPointerException when given product is null arg while deleting product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
        when:
            def out = m.deleteProduct(null)
        then:
            thrown(NullPointerException)
    }

    def "creates product ingredient"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
            def ingredient = new ProductIngredient()
            ingredient.id = 5000L
            ingredient.productId = 2137L
            ingredient.unit = Unit.Gram.toString()
            ingredient.quantity = 21.37
        when:
            m.createProductIngredient(ingredient)
        then:
            1 == 1
    }

    def "invokes toDotykackaProductIngredientMapper and productService.createProductIngredient while creating product ingredient"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
            m.toDotykackaProductIngredientMapper = Mock(CanonicalProductIngredientToDotykackaProductIngredientMapper)
        when:
            m.createProductIngredient(Mock(ProductIngredient))
        then:
            1 * m.toDotykackaProductIngredientMapper.apply(_)
        then:
            1 * m.productIngredientServiceFacade.createProductIngredient(_)
    }

    def "throws NullPointerException when ProductIngredient is null"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
        when:
            m.createProductIngredient(null)
        then:
            thrown(NullPointerException)
    }

    def "gets product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
            m.productService.getProduct(_) >> new Product()
        when:
            def out = m.getProduct(1L)
        then:
            out != null
    }

    def "invokes productService.getProduct(_) and toCanonicalProductMapper while getting product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
            m.toCanonicalProductMapper = Mock(DotykackaProductToCanonicalProductMapper)
        when:
            def out = m.getProduct(1L)
        then:
            1 * m.productService.getProduct(_) >> new Product()
        then:
            1 * m.toCanonicalProductMapper.apply(_)
    }

    def "throws NullPointerException when given id is null while getting the product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
        when:
            m.getProduct(null)
        then:
            thrown(NullPointerException)
    }

    def "updates product"() {
        given:
            def productService = Mock(ProductServiceFacade) {
                updateProduct(_) >> new Product()
            }
            def m = new DotykackaProductService(Mock(CategoryProvider), productService, productIngredientService, warehouseService)
        when:
            def out = m.updateProduct(new pl.grizzlysoftware.chlorek.core.model.Product())
        then:
            out != null
    }

    def "invokes toDotykackaProductMapper and toCanonicalProductMapper when updating product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade), productIngredientService, warehouseService)
            m.toDotykackaProductMapper = Mock(CanonicalProductToDotykackaProductMapper)
            m.toCanonicalProductMapper = Mock(DotykackaProductToCanonicalProductMapper)
        when:
            def out = m.updateProduct(new pl.grizzlysoftware.chlorek.core.model.Product())
        then:
            1 * m.toDotykackaProductMapper.apply(_)
        then:
            1 * m.productService.updateProduct(_) >> new Product()
        then:
            1 * m.toCanonicalProductMapper.apply(_)
    }

    def "throws NullPointerException when given product is null arg while updating product"() {
        given:
            def productService = Mock(ProductServiceFacade) {
                updateProduct(_) >> new Product()
            }
            def m = new DotykackaProductService(Mock(CategoryProvider), productService, productIngredientService, warehouseService)
        when:
            def out = m.updateProduct(null)
        then:
            thrown(NullPointerException)
    }

}
