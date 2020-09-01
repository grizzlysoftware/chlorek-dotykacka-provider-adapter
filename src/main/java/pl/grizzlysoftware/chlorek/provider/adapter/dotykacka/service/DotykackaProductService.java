package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.Product;
import pl.grizzlysoftware.chlorek.core.model.ProductIngredient;
import pl.grizzlysoftware.chlorek.core.model.ProductWithStockStatus;
import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider;
import pl.grizzlysoftware.chlorek.core.service.ProductService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductToCanonicalProductMapper;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductIngredientToDotykackaProductIngredientMapper;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductToDotykackaProductMapper;
import pl.grizzlysoftware.dotykacka.client.v1.facade.ProductServiceFacade;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaProductService implements ProductService {

    private ProductServiceFacade productService;
    private DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper toCanonicalProductWithStockStatusMapper;
    private DotykackaProductToCanonicalProductMapper toCanonicalProductMapper;
    private CanonicalProductToDotykackaProductMapper toDotykackaProductMapper;
    private CanonicalProductIngredientToDotykackaProductIngredientMapper toDotykackaProductIngredientMapper;

    public DotykackaProductService(CategoryProvider categoryProvider, ProductServiceFacade service) {
        this.productService = requireNonNull(service);

        this.toCanonicalProductWithStockStatusMapper = new DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper(categoryProvider);
        this.toCanonicalProductMapper = new DotykackaProductToCanonicalProductMapper(categoryProvider);
        this.toDotykackaProductMapper = new CanonicalProductToDotykackaProductMapper();
        this.toDotykackaProductIngredientMapper = new CanonicalProductIngredientToDotykackaProductIngredientMapper();
    }

    @Override
    public Collection<ProductWithStockStatus> getProductsWithStockStatus(long warehouseId, String sort) {
        var out = this.productService.getProductsWithStockStatus(warehouseId, sort)
                .stream()
                .map(toCanonicalProductWithStockStatusMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<ProductWithStockStatus> getProductsWithStockStatus(long warehouseId, int limit, int offset, String sort) {
        var out = this.productService.getProductsWithStockStatus(warehouseId, limit, offset, sort)
                .stream()
                .map(toCanonicalProductWithStockStatusMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<Product> getProducts(String sort) {
        var out = this.productService.getProducts(sort)
                .stream()
                .map(toCanonicalProductMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<Product> getProducts(int limit, int offset, String sort) {
        var out = this.productService.getProducts(limit, offset, sort)
                .stream()
                .map(toCanonicalProductMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Product createProduct(Product in) {
        var inDotykacka = toDotykackaProductMapper.apply(requireNonNull(in));
        var outDotykacka = productService.createProduct(inDotykacka);
        var out = toCanonicalProductMapper.apply(outDotykacka);
        return out;
    }

    @Override
    public Product deleteProduct(Long id) {
        var outDotykacka = productService.deleteProduct(requireNonNull(id));
        var out = toCanonicalProductMapper.apply(outDotykacka);
        return out;
    }

    @Override
    public void createProductIngredient(ProductIngredient in) {
        var out = toDotykackaProductIngredientMapper.apply(requireNonNull(in));
        productService.createProductIngredient(out);
    }

    @Override
    public Product getProduct(Long id) {
        var out = toCanonicalProductMapper.apply(productService.getProduct(requireNonNull(id)));
        return out;
    }

    @Override
    public Product updateProduct(Product product) {
        var outDotykacka = toDotykackaProductMapper.apply(requireNonNull(product));
        var out = toCanonicalProductMapper.apply(productService.updateProduct(outDotykacka));
        return out;
    }

    @Override
    public void saveAll(Collection<? extends Product> in) {
        throw new UnsupportedOperationException("AND SHOULD NOT BE SUPPORTED - at least not now");
    }

    @Override
    public void removeAll() {
        throw new UnsupportedOperationException("AND SHOULD NOT EVER BE SUPPORTED");
    }
}
