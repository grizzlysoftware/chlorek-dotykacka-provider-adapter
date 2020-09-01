package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.service;

import pl.grizzlysoftware.chlorek.core.model.Branch;
import pl.grizzlysoftware.chlorek.core.service.BranchService;
import pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.mapper.in.DotykackaBranchToCanonicalBranchMapper;
import pl.grizzlysoftware.dotykacka.client.v1.facade.BranchServiceFacade;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
public class DotykackaBranchService implements BranchService {

    private BranchServiceFacade service;
    private DotykackaBranchToCanonicalBranchMapper toCanonicalBranchMapper;

    public DotykackaBranchService(BranchServiceFacade service) {
        this.service = requireNonNull(service);
        this.toCanonicalBranchMapper = new DotykackaBranchToCanonicalBranchMapper();
    }

    @Override
    public Collection<Branch> getBranches() {
        var out = service.getBranches()
                .stream()
                .map(toCanonicalBranchMapper)
                .collect(toList());
        return out;
    }
}
