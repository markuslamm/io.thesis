package io.thesis.collector.dstat.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.dstat.AbstractDstatSampleCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses Dstat output given with the parameters "--proc", "--proc-count",
 * "--top-latency", "--top-latency-avg".
 */
public final class ProcessSampleCollector extends AbstractDstatSampleCollector {

    public static final String SAMPLE_KEY = "process";

    private static final Logger LOG = LoggerFactory.getLogger(ProcessSampleCollector.class);
    private static final String PROC_NAME_KEY = "name";
    private static final String PROC_VALUE_KEY = "value";
    private static final String PROC_RUNNABLE_KEY = "runnable";
    private static final String PROC_INTERRUPTIBLE_KEY = "uninterruptible";
    private static final String PROC_NEW_KEY = "new";
    private static final String PROC_COUNT_KEY = "count";
    private static final String PROC_LATENCY_HIGHEST_TOTAL_KEY = "latency-highest-total";
    private static final String PROC_LATENCY_HIGHEST_AVG_KEY = "latency-highest-avg";

    private static final Pattern PROC_STATS_PATTERN;
    private static final Pattern PROC_COUNT_PATTERN;
    private static final Pattern PROC_LATENCY_PATTERN;

    static {
        PROC_STATS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        PROC_COUNT_PATTERN = Pattern.compile("(\\d+)");

        PROC_LATENCY_PATTERN = Pattern.compile("" +
                "([a-zA-Z-_.:()/ ]+)(\\s*)" +
                "(\\d+(\\.\\d+)?)");
    }

    @Override
    protected Map<String, Object> applyData(final String[] dstatLines) {
        final Map<String, Object> processDataMap = Maps.newLinkedHashMap();
        final String[] columns = dstatLines[2].split("\\|");
        processDataMap.putAll(parseProcessStats(columns[25]));
        processDataMap.putAll(parseProcessCount(columns[26]));
        processDataMap.putAll(parseProcessLatencyHighestTotal(columns[27]));
        processDataMap.putAll(parseProcessLatencyHighestAvg(columns[28]));
        return processDataMap;
    }

    @Override
    protected Map<String, Object> createResultMap(final Map<String, Object> data) {
        final Map<String, Object> processResultMap = Maps.newLinkedHashMap();
        processResultMap.put(SAMPLE_KEY, data);
        return processResultMap;
    }

    private static Map<String, Object> parseProcessStats(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> processStatsData = Maps.newLinkedHashMap();
            final Matcher matcher = PROC_STATS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SAMPLE_KEY + " stats", raw);
            } else {
                try {
                    processStatsData.put(PROC_RUNNABLE_KEY, Float.valueOf(matcher.group(1)));
                    processStatsData.put(PROC_INTERRUPTIBLE_KEY, Float.valueOf(matcher.group(4)));
                    processStatsData.put(PROC_NEW_KEY, Float.valueOf(matcher.group(7)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", SAMPLE_KEY + " stats", ex.getMessage());
                }
            }
            return processStatsData;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseProcessCount(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> processCountData = Maps.newLinkedHashMap();
            final Matcher matcher = PROC_COUNT_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", PROC_COUNT_KEY, raw);
            } else {
                try {
                    processCountData.put(PROC_COUNT_KEY, Integer.valueOf(matcher.group(1)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", PROC_COUNT_KEY, ex.getMessage());
                }
            }
            return processCountData;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseProcessLatencyHighestTotal(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> processLatencyHighestTotalData = Maps.newLinkedHashMap();
            final Matcher matcher = PROC_LATENCY_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", PROC_LATENCY_HIGHEST_TOTAL_KEY, raw);
            } else {
                try {
                    processLatencyHighestTotalData.put(PROC_NAME_KEY, matcher.group(1).trim());
                    processLatencyHighestTotalData.put(PROC_VALUE_KEY, Float.valueOf(matcher.group(3).trim()));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", PROC_LATENCY_HIGHEST_TOTAL_KEY, ex.getMessage());

                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(PROC_LATENCY_HIGHEST_TOTAL_KEY, processLatencyHighestTotalData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseProcessLatencyHighestAvg(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> processLatencyHighestAvgData = Maps.newLinkedHashMap();
            final Matcher matcher = PROC_LATENCY_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", PROC_LATENCY_HIGHEST_AVG_KEY, raw);
            } else {
                try {
                    processLatencyHighestAvgData.put(PROC_NAME_KEY, matcher.group(1).trim());
                    processLatencyHighestAvgData.put(PROC_VALUE_KEY, Float.valueOf(matcher.group(3).trim()));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", PROC_LATENCY_HIGHEST_AVG_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(PROC_LATENCY_HIGHEST_AVG_KEY, processLatencyHighestAvgData);
            return result;
        }).orElse(Maps.newHashMap());
    }
}
