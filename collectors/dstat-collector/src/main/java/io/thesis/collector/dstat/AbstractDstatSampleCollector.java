package io.thesis.collector.dstat;

import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;

public abstract class AbstractDstatSampleCollector implements Function<String, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(final String dstatResult) {
        final String[] lines = dstatResult.split(System.lineSeparator());
        if (lines.length != 3) {
            throw new DStatCollectorException(format("Invalid input data, must be 3 lines, actual: %d", lines.length));
        }
        return createResultMap(applyData(lines));
    }

    protected abstract Map<String, Object> applyData(final String[] dstatLines);

    protected abstract Map<String, Object> createResultMap(final Map<String, Object> data);
}
