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
 * Parses Dstat output given with the parameters "--io", "--top-io-adv", "--lock", "--fs".
 */
public final class IoSampleCollector extends AbstractDstatSampleCollector {

    public static final String SAMPLE_KEY = "io";

    private static final Logger LOG = LoggerFactory.getLogger(IoSampleCollector.class);
    private static final String IO_NAME_KEY = "name";
    private static final String IO_READ_KEY = "read";
    private static final String IO_WRITE_KEY = "write";
    private static final String IO_PROCESS_MOST_EXP_KEY = "process-most-expensive";
    private static final String IO_PROCESS_MOST_EXP_PID_KEY = "pid";
    private static final String IO_PROCESS_MOST_EXP_CPU_PERCENTAGE_KEY = "cpuPercentage";
    private static final String IO_FILELOCKS_KEY = "filelocks";
    private static final String IO_FILELOCKS_POSIX = "posix";
    private static final String IO_FILELOCKS_FLOCK = "flock";
    private static final String IO_FILESYSTEM_KEY = "filesystem";
    private static final String IO_FILESYSTEM_OPEN_FILES_KEY = "openFiles";
    private static final String IO_FILESYSTEM_INODES_KEY = "inodes";

    private static final Pattern IO_STATS_PATTERN;
    private static final Pattern IO_MOST_EXPENSIVE_PROCESS_PATTERN;
    private static final Pattern IO_FILE_LOCKS_PATTERN;
    private static final Pattern IO_FILESYSTEM_PATTERN;

    static {
        IO_STATS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        IO_MOST_EXPENSIVE_PROCESS_PATTERN = Pattern.compile("" +
                "([a-zA-Z-_. ]+)(\\s*)" +
                "(\\d+)(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?)(%)");

        IO_FILE_LOCKS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        IO_FILESYSTEM_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?([k]?))");
    }

    @Override
    protected Map<String, Object> applyData(final String[] dstatLines) {
        final Map<String, Object> ioDataMap = Maps.newLinkedHashMap();
        final String[] columns = dstatLines[2].split("\\|");
        ioDataMap.putAll(parseIoReadWrite(columns[12]));
        ioDataMap.putAll(parseIoMostExpProcess(columns[13]));
        ioDataMap.putAll(parseIoFileLocks(columns[14]));
        ioDataMap.putAll(parseIoFilesystem(columns[15]));
        return ioDataMap;
    }

    @Override
    protected Map<String, Object> createResultMap(final Map<String, Object> data) {
        final Map<String, Object> ioResultMap = Maps.newLinkedHashMap();
        ioResultMap.put(SAMPLE_KEY, data);
        return ioResultMap;
    }

    private static Map<String, Object> parseIoReadWrite(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> ioReadWriteData = Maps.newLinkedHashMap();
            final Matcher matcher = IO_STATS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SAMPLE_KEY + " read/write", raw);
            } else {
                try {
                    ioReadWriteData.put(IO_READ_KEY, Float.valueOf(matcher.group(1)));
                    ioReadWriteData.put(IO_WRITE_KEY, Float.valueOf(matcher.group(4)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", SAMPLE_KEY + " read/write", ex.getMessage());
                }
            }
            return ioReadWriteData;

        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseIoMostExpProcess(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> ioMostExpProcessData = Maps.newLinkedHashMap();
            final Matcher matcher = IO_MOST_EXPENSIVE_PROCESS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", IO_PROCESS_MOST_EXP_KEY, raw);
            } else {
                try {
                    ioMostExpProcessData.put(IO_NAME_KEY, matcher.group(1).trim());
                    ioMostExpProcessData.put(IO_PROCESS_MOST_EXP_PID_KEY, Integer.valueOf(matcher.group(3).trim()));
                    ioMostExpProcessData.put(IO_READ_KEY, convertKilobytes(matcher.group(5).trim()));
                    ioMostExpProcessData.put(IO_WRITE_KEY, convertKilobytes(matcher.group(9).trim()));
                    ioMostExpProcessData.put(IO_PROCESS_MOST_EXP_CPU_PERCENTAGE_KEY, Float.valueOf(matcher.group(13).trim()));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", IO_PROCESS_MOST_EXP_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(IO_PROCESS_MOST_EXP_KEY, ioMostExpProcessData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseIoFileLocks(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> filelocksData = Maps.newLinkedHashMap();
            final Matcher matcher = IO_FILE_LOCKS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", IO_FILELOCKS_KEY, raw);
            } else {
                try {
                    filelocksData.put(IO_FILELOCKS_POSIX, Float.valueOf(matcher.group(1)));
                    filelocksData.put(IO_FILELOCKS_FLOCK, Float.valueOf(matcher.group(4)));
                    filelocksData.put(IO_READ_KEY, Float.valueOf(matcher.group(7)));
                    filelocksData.put(IO_WRITE_KEY, Float.valueOf(matcher.group(10)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", IO_FILELOCKS_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(IO_FILELOCKS_KEY, filelocksData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseIoFilesystem(final String rawData) {
        return Optional.ofNullable(rawData).map(o -> {
            final Map<String, Object> filesystemData = Maps.newLinkedHashMap();
            final Matcher matcher = IO_FILESYSTEM_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", IO_FILESYSTEM_KEY, rawData);
            } else {
                try {
                    filesystemData.put(IO_FILESYSTEM_OPEN_FILES_KEY, Integer.valueOf(matcher.group(1)));
                    filesystemData.put(IO_FILESYSTEM_INODES_KEY, convertThousand(matcher.group(4)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", IO_FILESYSTEM_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(IO_FILESYSTEM_KEY, filesystemData);
            return result;
        }).orElse(Maps.newHashMap());
    }
}
