package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util;

import java.util.Collection;
import java.util.function.Function;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class TagsToStringTagsMapper implements Function<Collection<String>, String> {
    @Override
    public String apply(Collection<String> in) {
        if (isEmpty(in)) {
            return "";
        }
        var out = join(in, ",");
        return out;
    }
}
