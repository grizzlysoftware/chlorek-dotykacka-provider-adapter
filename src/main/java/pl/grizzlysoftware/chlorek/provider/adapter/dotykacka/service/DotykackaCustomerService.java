package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.service.CustomerService;
import pl.grizzlysoftware.dotykacka.client.v2.facade.CustomerServiceFacade;

import static java.util.Objects.requireNonNull;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaCustomerService implements CustomerService {
    private final CustomerServiceFacade customerService;

    public DotykackaCustomerService(CustomerServiceFacade service) {
        this.customerService = requireNonNull(service);
    }
}
