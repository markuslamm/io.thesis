package io.thesis.collector.dstat.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.dstat.AbstractDstatSampleCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.thesis.collector.dstat.util.ConversionUtil.convertKilobytes;
import static io.thesis.collector.dstat.util.ConversionUtil.convertThousand;

/**
 * Parses Dstat output given with the parameters "--mem", "--top-mem", "--page",
 * "--swap", "--vm".
 */
public final class MemorySampleCollector extends AbstractDstatSampleCollector {

    public static final String SAMPLE_KEY = "memory";

    private static final Logger LOG = LoggerFactory.getLogger(MemorySampleCollector.class);
    private static final String MEMORY_USED_KEY = "used";
    private static final String MEMORY_FREE_KEY = "free";
    private static final String MEMORY_USAGE_KEY = "usage";
    private static final String MEMORY_USAGE_BUFFER_KEY = "buffer";
    private static final String MEMORY_USAGE_CACHE_KEY = "cache";
    private static final String MEMORY_PROCESS_MOST_EXP_KEY = "process-most-expensive";
    private static final String MEMORY_PROCESS_MOST_EXP_NAME_KEY = "name";
    private static final String MEMORY_PROCESS_MOST_EXP_VALUE_KEY = "value";
    private static final String MEMORY_PAGING_KEY = "paging";
    private static final String MEMORY_PAGING_IN_KEY = "in";
    private static final String MEMORY_PAGING_OUT_KEY = "out";
    private static final String MEMORY_SWAP_KEY = "swap";
    private static final String MEMORY_VM_KEY = "vm";
    private static final String MEMORY_VM_HPF_KEY = "hardPageFaults";
    private static final String MEMORY_VM_SPF_KEY = "softPageFaults";
    private static final String MEMORY_VM_ALLOCATED_KEY = "allocated";

    private static final Pattern MEMORY_STATS_PATTERN;
    private static final Pattern MEMORY_MOST_EXPENSIVE_PROCESS_PATTERN;
    private static final Pattern MEMORY_SWAP_PAGING_PATTERN;
    private static final Pattern MEMORY_VM_PATTERN;

    static {
        MEMORY_STATS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))");

        MEMORY_MOST_EXPENSIVE_PROCESS_PATTERN = Pattern.compile("" +
                "([a-zA-Z-_.:/ ]+)(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))");

        MEMORY_SWAP_PAGING_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))");

        MEMORY_VM_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))");
    }

    @Override
    protected Map<String, Object> applyData(final String[] dstatLines) {
        final Map<String, Object> memoryDataMap = Maps.newLinkedHashMap();
        final String[] columns = dstatLines[2].split("\\|");
        memoryDataMap.putAll(parseMemoryUsage(columns[16]));
        memoryDataMap.putAll(parseMemoryMostExpProcess(columns[17]));
        memoryDataMap.putAll(parseMemoryPaging(columns[18]));
        memoryDataMap.putAll(parseMemorySwap(columns[19]));
        memoryDataMap.putAll(parseMemoryVirtual(columns[20]));
        return memoryDataMap;
    }

    @Override
    protected Map<String, Object> createResultMap(final Map<String, Object> data) {
        final Map<String, Object> memoryResultMap = Maps.newLinkedHashMap();
        memoryResultMap.put(SAMPLE_KEY, data);
        return memoryResultMap;
    }

    private static Map<String, Object> parseMemoryUsage(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> memoryUsageData = Maps.newLinkedHashMap();
            final Matcher matcher = MEMORY_STATS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", MEMORY_USAGE_KEY, raw);
            } else {
                try {
                    memoryUsageData.put(MEMORY_USED_KEY, convertKilobytes(matcher.group(1)));
                    memoryUsageData.put(MEMORY_USAGE_BUFFER_KEY, convertKilobytes(matcher.group(5)));
                    memoryUsageData.put(MEMORY_USAGE_CACHE_KEY, convertKilobytes(matcher.group(9)));
                    memoryUsageData.put(MEMORY_FREE_KEY, convertKilobytes(matcher.group(13)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", MEMORY_USAGE_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(MEMORY_USAGE_KEY, memoryUsageData);
            return result;
        }).orElse(Maps.newHashMap());
    }


    private static Map<String, Object> parseMemoryMostExpProcess(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> memMostExpProcessData = Maps.newLinkedHashMap();
            final Matcher matcher = MEMORY_MOST_EXPENSIVE_PROCESS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", MEMORY_PROCESS_MOST_EXP_KEY, raw);
            } else {
                try {
                    memMostExpProcessData.put(MEMORY_PROCESS_MOST_EXP_NAME_KEY, matcher.group(1).trim());
                    memMostExpProcessData.put(MEMORY_PROCESS_MOST_EXP_VALUE_KEY, convertKilobytes(matcher.group(3).trim()));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", MEMORY_PROCESS_MOST_EXP_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(MEMORY_PROCESS_MOST_EXP_KEY, memMostExpProcessData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseMemoryPaging(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> memPagingData = Maps.newLinkedHashMap();
            final Matcher matcher = MEMORY_SWAP_PAGING_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", MEMORY_PAGING_KEY, raw);
            } else {
                try {
                    memPagingData.put(MEMORY_PAGING_IN_KEY, convertKilobytes(matcher.group(1)));
                    memPagingData.put(MEMORY_PAGING_OUT_KEY, convertKilobytes(matcher.group(5)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", MEMORY_PAGING_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(MEMORY_PAGING_KEY, memPagingData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseMemorySwap(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> memSwapData = Maps.newLinkedHashMap();
            final Matcher matcher = MEMORY_SWAP_PAGING_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", MEMORY_SWAP_KEY, raw);
            } else {
                try {
                    memSwapData.put(MEMORY_USED_KEY, convertKilobytes(matcher.group(1)));
                    memSwapData.put(MEMORY_FREE_KEY, convertKilobytes(matcher.group(5)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", MEMORY_SWAP_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(MEMORY_SWAP_KEY, memSwapData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseMemoryVirtual(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> memVmData = Maps.newLinkedHashMap();
            final Matcher matcher = MEMORY_VM_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", MEMORY_VM_KEY, raw);
            } else {
                try {
                    memVmData.put(MEMORY_VM_HPF_KEY, convertThousand(matcher.group(1)));
                    memVmData.put(MEMORY_VM_SPF_KEY, convertThousand(matcher.group(5)));
                    memVmData.put(MEMORY_VM_ALLOCATED_KEY, convertThousand(matcher.group(9)));
                    memVmData.put(MEMORY_FREE_KEY, convertThousand(matcher.group(13)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", MEMORY_VM_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(MEMORY_VM_KEY, memVmData);
            return result;
        }).orElse(Maps.newHashMap());
    }
}
