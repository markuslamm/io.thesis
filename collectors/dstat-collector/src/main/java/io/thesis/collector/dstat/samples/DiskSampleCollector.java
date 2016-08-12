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

/**
 * Parses Dstat output given with the parameters "--disk", "--disk-tps", "--disk-util".
 */
public final class DiskSampleCollector extends AbstractDstatSampleCollector {

    public static final String SAMPLE_KEY = "disk";

    private static final Logger LOG = LoggerFactory.getLogger(DiskSampleCollector.class);
    private static final String DISK_READ_KEY = "read";
    private static final String DISK_WRITE_KEY = "write";
    private static final String DISK_UTILIZATION_KEY = "utilization";
    private static final String DISK_TRANSACTIONS_KEY = "transactions";

    private static final Pattern DISK_READ_WRITE_PATTERN;
    private static final Pattern DISK_TRANSACTIONS_PATTERN;
    private static final Pattern DISK_UTILIZATION_PATTERN;

    static {
        DISK_READ_WRITE_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))");

        DISK_TRANSACTIONS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        DISK_UTILIZATION_PATTERN = Pattern.compile("(\\d+(\\.\\d+)?)");
    }

    @Override
    protected Map<String, Object> applyData(final String[] dstatLines) {
        final String[] columns = dstatLines[2].split("\\|");
        final Map<String, Object> diskDataMap = Maps.newLinkedHashMap();
        diskDataMap.putAll(parseDiskReadWrite(columns[5]));
        diskDataMap.putAll(parseDiskUtilization(columns[7]));
        diskDataMap.putAll(parseDiskTransactionsReadWrite(columns[6]));
        return diskDataMap;
    }

    @Override
    protected Map<String, Object> createResultMap(final Map<String, Object> data) {
        final Map<String, Object> diskResultMap = Maps.newLinkedHashMap();
        diskResultMap.put(SAMPLE_KEY, data);
        return diskResultMap;
    }

    private static Map<String, Object> parseDiskReadWrite(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> diskReadWriteData = Maps.newLinkedHashMap();
            final Matcher matcher = DISK_READ_WRITE_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SAMPLE_KEY + "-read/write", raw);
            } else {
                try {
                    diskReadWriteData.put(DISK_READ_KEY, convertKilobytes(matcher.group(1)));
                    diskReadWriteData.put(DISK_WRITE_KEY, convertKilobytes(matcher.group(5)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", SAMPLE_KEY + "-read/write", ex.getMessage());
                }
            }
            return diskReadWriteData;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseDiskTransactionsReadWrite(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> diskTransactionsData = Maps.newLinkedHashMap();
            final Matcher matcher = DISK_TRANSACTIONS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", DISK_TRANSACTIONS_KEY, raw);
            } else {
                try {
                    diskTransactionsData.put(DISK_READ_KEY, Float.valueOf(matcher.group(1)));
                    diskTransactionsData.put(DISK_WRITE_KEY, Float.valueOf(matcher.group(4)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", DISK_TRANSACTIONS_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(DISK_TRANSACTIONS_KEY, diskTransactionsData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseDiskUtilization(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> diskUtilizationData = Maps.newLinkedHashMap();
            final Matcher matcher = DISK_UTILIZATION_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", DISK_UTILIZATION_KEY, rawData);
            } else {
                try {
                    diskUtilizationData.put(DISK_UTILIZATION_KEY, Float.valueOf(matcher.group(1)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", DISK_UTILIZATION_KEY, ex.getMessage());
                }
            }
            return diskUtilizationData;
        }).orElse(Maps.newHashMap());
    }
}
