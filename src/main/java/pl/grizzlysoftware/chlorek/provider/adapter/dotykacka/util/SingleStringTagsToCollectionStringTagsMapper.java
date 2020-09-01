package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SingleStringTagsToCollectionStringTagsMapper implements Function<String, Collection<String>> {
    @Override
    public Collection<String> apply(String in) {
        var out = isEmpty(in) ? new ArrayList<>() : new ArrayList<>(asList(in.split(",")));
        return (Collection<String>) out;
    }
}
