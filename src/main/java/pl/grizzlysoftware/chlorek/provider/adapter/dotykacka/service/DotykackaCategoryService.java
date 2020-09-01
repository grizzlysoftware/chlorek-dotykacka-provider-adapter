package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.Category;
import pl.grizzlysoftware.chlorek.core.service.CategoryService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaCategoryToCanonicalCategoryMapper;
import pl.grizzlysoftware.dotykacka.client.v1.facade.CategoryServiceFacade;

import java.util.Collection;
import java.util.Comparator;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static pl.grizzlysoftware.commons.ComparatorUtils.stringComparator;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaCategoryService implements CategoryService {

    private CategoryServiceFacade service;
    private DotykackaCategoryToCanonicalCategoryMapper toCanonicalCategoryMapper;

    public DotykackaCategoryService(CategoryServiceFacade service) {
        this.service = requireNonNull(service);
        this.toCanonicalCategoryMapper = new DotykackaCategoryToCanonicalCategoryMapper();
    }

    @Override
    public Collection<Category> getCategories(int limit, int offset, String order) {
        var out = service.getCategories(limit, offset, order)
                .stream()
                .map(toCanonicalCategoryMapper)
                .sorted(Comparator.comparing(e -> e.name))
                .collect(toList());
        return out;
    }

    @Override
    public Collection<Category> getCategories() {
        var out = service.getCategories()
                .stream()
                .map(toCanonicalCategoryMapper)
                .sorted(stringComparator(e -> e.name))
                .collect(toList());
        return out;
    }

    @Override
    public Category getCategory(Long id) {
        var out = toCanonicalCategoryMapper.apply(service.getCategory(id));
        return out;
    }
}
