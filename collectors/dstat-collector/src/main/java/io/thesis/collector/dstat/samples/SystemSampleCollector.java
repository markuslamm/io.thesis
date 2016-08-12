package io.thesis.collector.dstat.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.dstat.AbstractDstatSampleCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.thesis.collector.dstat.util.ConversionUtil.convertThousand;

/**
 * Parses Dstat output given with the parameters "--sys", "--load",
 * "--ipc", "--unix".
 */
public final class SystemSampleCollector extends AbstractDstatSampleCollector {

    public static final String SAMPLE_KEY = "system";

    private static final Logger LOG = LoggerFactory.getLogger(SystemSampleCollector.class);
    private static final String SYSTEM_INTERRUPTS_KEY = "interrupts";
    private static final String SYSTEM_CS_KEY = "contextSwitches";
    private static final String SYSTEM_LOAD_AVG_KEY = "load-avg";
    private static final String SYSTEM_LOAD_AVG_1M_KEY = "1m";
    private static final String SYSTEM_LOAD_AVG_5M_KEY = "5m";
    private static final String SYSTEM_LOAD_AVG_15M_KEY = "15m";
    private static final String SYSTEM_IPC_KEY = "ipc";
    private static final String SYSTEM_IPC_MESSAGE_QUEUE_KEY = "messageQueue";
    private static final String SYSTEM_IPC_SEMAPHORES_KEY = "semaphores";
    private static final String SYSTEM_IPC_SHARED_MEMORY_KEY = "sharedMemory";
    private static final String SYSTEM_UNIX_SOCKETS_KEY = "unix-sockets";
    private static final String SYSTEM_UNIX_SOCKETS_DATAGRAM_KEY = "datagram";
    private static final String SYSTEM_UNIX_SOCKETS_STREAM_KEY = "stream";
    private static final String SYSTEM_UNIX_SOCKETS_LISTEN_KEY = "listen";
    private static final String SYSTEM_UNIX_SOCKETS_ACTIVE_KEY = "active";

    private static final Pattern SYSTEM_INTERRUPTS_CS_PATTERN;
    private static final Pattern SYSTEM_LOAD_AVG_PATTERN;
    private static final Pattern SYSTEM_IPC_PATTERN;
    private static final Pattern SYSTEM_UNIX_PATTERN;

    static {
        SYSTEM_INTERRUPTS_CS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)([k]?)(\\s*)" +
                "(\\d+(\\.\\d+)?)([k]?)");

        SYSTEM_LOAD_AVG_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        SYSTEM_IPC_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        SYSTEM_UNIX_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");
    }

    @Override
    protected Map<String, Object> applyData(final String[] dstatLines) {
        final Map<String, Object> systemDataMap = Maps.newLinkedHashMap();
        final String[] columns = dstatLines[2].split("\\|");
        systemDataMap.putAll(parseSystemLoadAvg(columns[22]));
        systemDataMap.putAll(parseSystemStats(columns[21]));
        systemDataMap.putAll(parseSystemIpc(columns[23]));
        systemDataMap.putAll(parseSystemUnixSockets(columns[24]));
        return systemDataMap;
    }

    @Override
    protected Map<String, Object> createResultMap(final Map<String, Object> data) {
        final Map<String, Object> systemResultMap = Maps.newLinkedHashMap();
        systemResultMap.put(SAMPLE_KEY, data);
        return systemResultMap;
    }

    private static Map<String, Object> parseSystemStats(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> systemInterruptsCsData = Maps.newLinkedHashMap();
            final Matcher matcher = SYSTEM_INTERRUPTS_CS_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SAMPLE_KEY + " interrupts/context switches", rawData);
            } else {
                try {
                    systemInterruptsCsData.put(SYSTEM_INTERRUPTS_KEY, Float.valueOf(matcher.group(1)));
                    systemInterruptsCsData.put(SYSTEM_CS_KEY, convertThousand(matcher.group(5)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", SAMPLE_KEY + " interrupts/context switches", ex.getMessage());
                }
            }
            return systemInterruptsCsData;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseSystemLoadAvg(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> systemLoadAvgData = Maps.newLinkedHashMap();
            final Matcher matcher = SYSTEM_LOAD_AVG_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SYSTEM_LOAD_AVG_KEY, rawData);
            } else {
                try {
                    systemLoadAvgData.put(SYSTEM_LOAD_AVG_1M_KEY, Float.valueOf(matcher.group(1)));
                    systemLoadAvgData.put(SYSTEM_LOAD_AVG_5M_KEY, Float.valueOf(matcher.group(4)));
                    systemLoadAvgData.put(SYSTEM_LOAD_AVG_15M_KEY, Float.valueOf(matcher.group(7)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", SYSTEM_LOAD_AVG_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(SYSTEM_LOAD_AVG_KEY, systemLoadAvgData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseSystemIpc(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> systemIpcData = Maps.newLinkedHashMap();
            final Matcher matcher = SYSTEM_IPC_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SYSTEM_IPC_KEY, rawData);
            } else {
                try {
                    systemIpcData.put(SYSTEM_IPC_MESSAGE_QUEUE_KEY, Float.valueOf(matcher.group(1)));
                    systemIpcData.put(SYSTEM_IPC_SEMAPHORES_KEY, Float.valueOf(matcher.group(4)));
                    systemIpcData.put(SYSTEM_IPC_SHARED_MEMORY_KEY, Float.valueOf(matcher.group(7)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", SYSTEM_IPC_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(SYSTEM_IPC_KEY, systemIpcData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseSystemUnixSockets(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> systemUnixSocketsData = Maps.newLinkedHashMap();
            final Matcher matcher = SYSTEM_UNIX_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SYSTEM_UNIX_SOCKETS_KEY, rawData);
            } else {
                try {
                    systemUnixSocketsData.put(SYSTEM_UNIX_SOCKETS_DATAGRAM_KEY, Integer.valueOf(matcher.group(1)));
                    systemUnixSocketsData.put(SYSTEM_UNIX_SOCKETS_STREAM_KEY, Integer.valueOf(matcher.group(4)));
                    systemUnixSocketsData.put(SYSTEM_UNIX_SOCKETS_LISTEN_KEY, Integer.valueOf(matcher.group(7)));
                    systemUnixSocketsData.put(SYSTEM_UNIX_SOCKETS_ACTIVE_KEY, Integer.valueOf(matcher.group(10)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", SYSTEM_UNIX_SOCKETS_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(SYSTEM_UNIX_SOCKETS_KEY, systemUnixSocketsData);
            return result;
        }).orElse(Maps.newHashMap());
    }
}
