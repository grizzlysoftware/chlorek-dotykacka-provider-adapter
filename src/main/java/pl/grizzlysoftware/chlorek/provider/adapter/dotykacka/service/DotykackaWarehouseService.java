package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.Warehouse;
import pl.grizzlysoftware.chlorek.core.service.WarehouseService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaWarehouseToCanonicalWarehouseMapper;
import pl.grizzlysoftware.dotykacka.client.v2.facade.WarehouseServiceFacade;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaWarehouseService implements WarehouseService {

    private WarehouseServiceFacade service;
    private DotykackaWarehouseToCanonicalWarehouseMapper toCanonicalWarehouseMapper;

    public DotykackaWarehouseService(WarehouseServiceFacade service) {
        this.service = requireNonNull(service);
        this.toCanonicalWarehouseMapper = new DotykackaWarehouseToCanonicalWarehouseMapper();
    }

    @Override
    public Collection<Warehouse> getWarehouses() {
        var out = service.getAllWarehouses()
                .stream()
                .map(toCanonicalWarehouseMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Collection<Warehouse> getWarehouses(int page, int pageSize) {
        var out = service.getWarehouses(page, pageSize, null)
                .data
                .stream()
                .map(toCanonicalWarehouseMapper)
                .collect(toList());
        return out;
    }

    @Override
    public Warehouse getWarehouse(Long id) {
        var out = toCanonicalWarehouseMapper.apply(service.getWarehouse(id));
        return out;
    }
}
