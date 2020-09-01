package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service

import pl.grizzlysoftware.chlorek.core.model.Category
import pl.grizzlysoftware.chlorek.core.model.ProductIngredient
import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductToCanonicalProductMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductIngredientToDotykackaProductIngredientMapper
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductToDotykackaProductMapper
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.product.Product
import pl.grizzlysoftware.dotykacka.client.v1.api.dto.product.ProductWithStockStatus
import pl.grizzlysoftware.dotykacka.client.v1.facade.ProductServiceFacade
import spock.lang.Specification

class DotykackaProductServiceTest extends Specification {
    def "throws exception when given args are null"(categoryProvider, productService) {
        when:
            new DotykackaProductService(categoryProvider, productService)
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
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProductsWithStockStatus(_, _) >> [
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
        when:
            def out = m.getProductsWithStockStatus(0, null)
        then:
            out != null
            out.size() == 6
    }

    def "returns products with stock status #2"() {
        given:
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProductsWithStockStatus(_, _, _, _) >> [
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
        when:
            def out = m.getProductsWithStockStatus(0, 0, 0, null)
        then:
            out != null
            out.size() == 6
    }

    def "invokes toCanonicalProductWithStockStatusMapper for each existing ProductWithStockStatus #1"() {
        given:
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProductsWithStockStatus(_, _) >> [
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
            m.toCanonicalProductWithStockStatusMapper = Mock(DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper)

        when:
            def out = m.getProductsWithStockStatus(0, null)
        then:
            6 * m.toCanonicalProductWithStockStatusMapper.apply(_)
    }

    def "invokes toCanonicalProductWithStockStatusMapper for each existing ProductWithStockStatus #2"() {
        given:
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProductsWithStockStatus(_, _, _, _) >> [
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus),
                        Mock(ProductWithStockStatus), Mock(ProductWithStockStatus)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
            m.toCanonicalProductWithStockStatusMapper = Mock(DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper)
        when:
            def out = m.getProductsWithStockStatus(0, 0, 0, null)
        then:
            6 * m.toCanonicalProductWithStockStatusMapper.apply(_)
    }

    def "returns products #1"() {
        given:
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProducts(_) >> [
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
        when:
            def out = m.getProducts(null)
        then:
            out != null
            out.size() == 6
    }

    def "returns products #2"() {
        given:
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProducts(_, _, _) >> [
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
        when:
            def out = m.getProducts(0, 0, null)
        then:
            out != null
            out.size() == 6
    }

    def "invokes toCanonicalProductMapper for each existing Product #1"() {
        given:
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProducts(_) >> [
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
            m.toCanonicalProductMapper = Mock(DotykackaProductToCanonicalProductMapper)
        when:
            def out = m.getProducts(null)
        then:
            6 * m.toCanonicalProductMapper.apply(_)
    }

    def "invokes toCanonicalProductMapper for each existing Product #2"() {
        given:
            def categoryProvider = Mock(CategoryProvider) {
                getCategory(_) >> new Category()
            }
            def productService = Mock(ProductServiceFacade) {
                getProducts(_, _, _) >> [
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product),
                        Mock(Product), Mock(Product)
                ]
            }
            def m = new DotykackaProductService(categoryProvider, productService)
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
            def m = new DotykackaProductService(Mock(CategoryProvider), productService)
        when:
            def out = m.createProduct(new pl.grizzlysoftware.chlorek.core.model.Product())
        then:
            out != null
    }

    def "invokes toDotykackaProductMapper and toCanonicalProductMapper when creating product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
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
            def m = new DotykackaProductService(Mock(CategoryProvider), productService)
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
            def m = new DotykackaProductService(Mock(CategoryProvider), productService)
        when:
            def out = m.deleteProduct(1L)
        then:
            out != null
    }

    def "invokes productService.deleteProduct and toCanonicalProductMapper while deleting product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
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
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
        when:
            def out = m.deleteProduct(null)
        then:
            thrown(NullPointerException)
    }

    def "creates product ingredient"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
        when:
            m.createProductIngredient(Mock(ProductIngredient))
        then:
            1 == 1
    }

    def "invokes toDotykackaProductIngredientMapper and productService.createProductIngredient while creating product ingredient"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
            m.toDotykackaProductIngredientMapper = Mock(CanonicalProductIngredientToDotykackaProductIngredientMapper)
        when:
            m.createProductIngredient(Mock(ProductIngredient))
        then:
            1 * m.toDotykackaProductIngredientMapper.apply(_)
        then:
            1 * m.productService.createProductIngredient(_)
    }

    def "throws NullPointerException when ProductIngredient is null"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
        when:
            m.createProductIngredient(null)
        then:
            thrown(NullPointerException)
    }

    def "gets product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
            m.productService.getProduct(_) >> new Product()
        when:
            def out = m.getProduct(1L)
        then:
            out != null
    }
    
    def "invokes productService.getProduct(_) and toCanonicalProductMapper while getting product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
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
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
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
            def m = new DotykackaProductService(Mock(CategoryProvider), productService)
        when:
            def out = m.updateProduct(new pl.grizzlysoftware.chlorek.core.model.Product())
        then:
            out != null
    }

    def "invokes toDotykackaProductMapper and toCanonicalProductMapper when updating product"() {
        given:
            def m = new DotykackaProductService(Mock(CategoryProvider), Mock(ProductServiceFacade))
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
            def m = new DotykackaProductService(Mock(CategoryProvider), productService)
        when:
            def out = m.updateProduct(null)
        then:
            thrown(NullPointerException)
    }

}
