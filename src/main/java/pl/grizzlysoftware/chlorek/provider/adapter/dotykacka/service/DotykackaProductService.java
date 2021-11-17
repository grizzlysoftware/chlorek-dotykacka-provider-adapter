package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import lombok.extern.slf4j.Slf4j;
import pl.grizzlysoftware.chlorek.core.model.Product;
import pl.grizzlysoftware.chlorek.core.model.ProductIngredient;
import pl.grizzlysoftware.chlorek.core.model.ProductWithStockStatus;
import pl.grizzlysoftware.chlorek.core.provider.CategoryProvider;
import pl.grizzlysoftware.chlorek.core.service.ProductService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductToCanonicalProductMapper;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductIngredientToDotykackaProductIngredientMapper;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.out.CanonicalProductToDotykackaProductMapper;
import pl.grizzlysoftware.dotykacka.client.v2.facade.ProductIngredientServiceFacade;
import pl.grizzlysoftware.dotykacka.client.v2.facade.ProductServiceFacade;
import pl.grizzlysoftware.dotykacka.client.v2.facade.WarehouseServiceFacade;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
@Slf4j
public class DotykackaProductService implements ProductService {

    private ProductServiceFacade productService;
    private ProductIngredientServiceFacade productIngredientServiceFacade;
    private WarehouseServiceFacade warehouseServiceFacade;


    private DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper toCanonicalProductWithStockStatusMapper;
    private DotykackaProductToCanonicalProductMapper toCanonicalProductMapper;
    private CanonicalProductToDotykackaProductMapper toDotykackaProductMapper;
    private CanonicalProductIngredientToDotykackaProductIngredientMapper toDotykackaProductIngredientMapper;

    public DotykackaProductService(CategoryProvider categoryProvider, ProductServiceFacade service,
                                   ProductIngredientServiceFacade productIngredientServiceFacade, WarehouseServiceFacade warehouseServiceFacade) {
        this.productService = requireNonNull(service);
        this.productIngredientServiceFacade = requireNonNull(productIngredientServiceFacade);
        this.warehouseServiceFacade = requireNonNull(warehouseServiceFacade);

        this.toCanonicalProductWithStockStatusMapper = new DotykackaProductWithStockStatusToCanonicalProductWithStockStatusMapper(categoryProvider);
        this.toCanonicalProductMapper = new DotykackaProductToCanonicalProductMapper(categoryProvider);
        this.toDotykackaProductMapper = new CanonicalProductToDotykackaProductMapper();
        this.toDotykackaProductIngredientMapper = new CanonicalProductIngredientToDotykackaProductIngredientMapper();
    }

    @Override
    public Collection<ProductWithStockStatus> getProductsWithStockStatus(long warehouseId, String sort) {
        var out = this.warehouseServiceFacade.getProductStocks(warehouseId, 0, 100, null, sort)
                .data
                .stream()
                .map(toCanonicalProductWithStockStatusMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<ProductWithStockStatus> getProductsWithStockStatus(long warehouseId, int page, int pageSize, String sort) {
        var out = this.warehouseServiceFacade.getProductStocks(warehouseId, page, pageSize, null, sort)
                .data
                .stream()
                .map(toCanonicalProductWithStockStatusMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<Product> getProducts(String sort) {
        var out = this.productService.getAllProducts(sort)
                .stream()
                .map(toCanonicalProductMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<Product> getProducts(int page, int pageSize, String sort) {
        var out = this.productService.getProducts(page + 1, pageSize, null, null, sort)
                .data
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
        productIngredientServiceFacade.createProductIngredient(out);
    }

    @Override
    public Product getProduct(Long id) {
        var out = toCanonicalProductMapper.apply(productService.getProduct(requireNonNull(id)));
        return out;
    }

    @Override
    public Collection<Product> getProductsByEan(String s) {
        return productService.getAllProducts("", "ean=" + s, null)
                .stream()
                .map(toCanonicalProductMapper)
                .collect(toList());
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
