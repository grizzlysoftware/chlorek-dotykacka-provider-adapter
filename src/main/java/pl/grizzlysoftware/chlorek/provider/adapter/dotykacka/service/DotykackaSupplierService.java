package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.Supplier;
import pl.grizzlysoftware.chlorek.core.service.SupplierService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaSupplierToCanonicalSupplierMapper;
import pl.grizzlysoftware.dotykacka.client.v2.facade.SupplierServiceFacade;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static pl.grizzlysoftware.commons.ComparatorUtils.stringComparator;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaSupplierService implements SupplierService {

    private SupplierServiceFacade supplierService;
    private DotykackaSupplierToCanonicalSupplierMapper toCanonicalSupplierMapper;

    public DotykackaSupplierService(SupplierServiceFacade service) {
        this.supplierService = requireNonNull(service);
        this.toCanonicalSupplierMapper = new DotykackaSupplierToCanonicalSupplierMapper();
    }

    @Override
    public Collection<Supplier> getSuppliers() {
        var out = supplierService.getAllSuppliers()
                .stream()
                .map(toCanonicalSupplierMapper)
                .sorted(stringComparator(e -> e.name))
                .collect(toList());
        return out;
    }
}
