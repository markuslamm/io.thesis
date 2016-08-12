package io.thesis.collector.dstat.samples;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.thesis.collector.dstat.AbstractDstatSampleCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.thesis.collector.dstat.util.ConversionUtil.convertKilobytes;

/**
 * Parses Dstat output given with the parameters "--cpu", "--top-cpu-adv",
 * "--top-cputime", "--top-cputime-avg".
 */
public final class CpuSampleCollector extends AbstractDstatSampleCollector {

    public static final String SAMPLE_KEY = "cpu";

    private static final Logger LOG = LoggerFactory.getLogger(CpuSampleCollector.class);
    private static final String CPU_NAME_KEY = "name";
    private static final String CPU_TIME_KEY = "time";
    private static final String CPU_USAGE_KEY = "usage";
    private static final String CPU_USAGE_USER_KEY = "user";
    private static final String CPU_USAGE_SYSTEM_KEY = "system";
    private static final String CPU_USAGE_IDLE_KEY = "idle";
    private static final String CPU_USAGE_WAIT_KEY = "wait";
    private static final String CPU_USAGE_HW_INTERRUPT_KEY = "hwInterrupt";
    private static final String CPU_USAGE_SW_INTERRUPT_KEY = "swInterrupt";
    private static final String CPU_PROCESS_MOST_EXP_KEY = "process-most-expensive";
    private static final String CPU_PROCESS_MOST_EXP_PID_KEY = "pid";
    private static final String CPU_PROCESS_MOST_EXP_CPU_PERCENTAGE_KEY = "cpuPercentage";
    private static final String CPU_PROCESS_MOST_EXP_READ_KEY = "read";
    private static final String CPU_PROCESS_MOST_EXP_WRITE_KEY = "write";
    private static final String CPU_PROCESS_HIGHEST_TOTAL_KEY = "process-cpu-time-highest-total";
    private static final String CPU_PROCESS_HIGHEST_AVG_KEY = "process-cpu-time-highest-avg";

    private static final Pattern CPU_USAGE_PATTERN;
    private static final Pattern CPU_MOST_EXPENSIVE_PROCESS_PATTERN;
    private static final Pattern CPU_TIME_PATTERN;

    static {
        CPU_USAGE_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        CPU_MOST_EXPENSIVE_PROCESS_PATTERN = Pattern.compile("" +
                "([a-zA-Z-_.:/() ]+)(\\s*)" +
                "(\\d+)(\\s*)" +
                "(\\d+(\\.\\d+)?)(%)(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))");

        CPU_TIME_PATTERN = Pattern.compile("" +
                "([a-zA-Z-_.:/() ]+)(\\s*)" +
                "(\\d+(\\.\\d+)?)");
    }

    @Override
    protected Map<String, Object> applyData(final String[] dstatLines) {
        final String[] columns = dstatLines[2].split("\\|");
        final Map<String, Object> cpuDataMap = Maps.newLinkedHashMap();
        cpuDataMap.putAll(parseCpuUsages(columns[1]));
        cpuDataMap.putAll(parseCpuMostExpProcess(columns[2]));
        cpuDataMap.putAll(parseCpuTime(columns[3], CPU_PROCESS_HIGHEST_TOTAL_KEY));
        cpuDataMap.putAll(parseCpuTime(columns[4], CPU_PROCESS_HIGHEST_AVG_KEY));
        return cpuDataMap;
    }

    @Override
    protected Map<String, Object> createResultMap(final Map<String, Object> data) {
        final Map<String, Object> cpuResultMap = Maps.newLinkedHashMap();
        cpuResultMap.put(SAMPLE_KEY, data);
        return cpuResultMap;
    }

    private static Map<String, Object> parseCpuUsage(final String rawData, final String cpuName) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Matcher matcher = CPU_USAGE_PATTERN.matcher(raw.trim());
            final Map<String, Object> cpuUsageMap = Maps.newLinkedHashMap();
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", CPU_USAGE_KEY, raw);
            } else {
                try {
                    cpuUsageMap.put(CPU_NAME_KEY, cpuName);
                    cpuUsageMap.put(CPU_USAGE_USER_KEY, Float.valueOf(matcher.group(1)));
                    cpuUsageMap.put(CPU_USAGE_SYSTEM_KEY, Float.valueOf(matcher.group(4)));
                    cpuUsageMap.put(CPU_USAGE_IDLE_KEY, Float.valueOf(matcher.group(7)));
                    cpuUsageMap.put(CPU_USAGE_WAIT_KEY, Float.valueOf(matcher.group(10)));
                    cpuUsageMap.put(CPU_USAGE_HW_INTERRUPT_KEY, Float.valueOf(matcher.group(13)));
                    cpuUsageMap.put(CPU_USAGE_SW_INTERRUPT_KEY, Float.valueOf(matcher.group(16)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", CPU_USAGE_KEY, ex.getMessage());
                }
            }
            return cpuUsageMap;

        }).orElse(Maps.newHashMap());
    }

    private static Map<String, List<Map<String, Object>>> parseCpuUsages(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final List<Map<String, Object>> cpuUsagesList = Lists.newLinkedList();
            if (raw.contains(":")) {
                final String[] cpus = raw.split(":");
                for (int i = 0; i <= cpus.length - 1; i++) {
                    cpuUsagesList.add(parseCpuUsage(cpus[i], createCpuName(i)));
                }
            } else {
                cpuUsagesList.add(parseCpuUsage(raw, createCpuName(0)));
            }
            final Map<String, List<Map<String, Object>>> cpuUsagesMap = Maps.newLinkedHashMap();
            cpuUsagesMap.put(CPU_USAGE_KEY, cpuUsagesList);
            return cpuUsagesMap;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseCpuMostExpProcess(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Matcher matcher = CPU_MOST_EXPENSIVE_PROCESS_PATTERN.matcher(raw.trim());
            final Map<String, Object> cpuMostExpProcessData = Maps.newLinkedHashMap();
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", CPU_PROCESS_MOST_EXP_KEY, raw);
            } else {
                try {
                    cpuMostExpProcessData.put(CPU_NAME_KEY, matcher.group(1).trim());
                    cpuMostExpProcessData.put(CPU_PROCESS_MOST_EXP_PID_KEY, Integer.valueOf(matcher.group(3).trim()));
                    cpuMostExpProcessData.put(CPU_PROCESS_MOST_EXP_CPU_PERCENTAGE_KEY, Float.valueOf(matcher.group(5).trim()));
                    cpuMostExpProcessData.put(CPU_PROCESS_MOST_EXP_READ_KEY, convertKilobytes(matcher.group(9).trim()));
                    cpuMostExpProcessData.put(CPU_PROCESS_MOST_EXP_WRITE_KEY, convertKilobytes(matcher.group(13).trim()));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", CPU_PROCESS_MOST_EXP_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(CPU_PROCESS_MOST_EXP_KEY, cpuMostExpProcessData);
            return result;
        }).orElse(Maps.newHashMap());

    }

    private static Map<String, Object> parseCpuTime(final String rawData, final String key) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Matcher matcher = CPU_TIME_PATTERN.matcher(raw.trim());
            final Map<String, Object> cpuTimeData = Maps.newLinkedHashMap();
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", key, raw);
            } else {
                try {
                    cpuTimeData.put(CPU_NAME_KEY, matcher.group(1).trim());
                    cpuTimeData.put(CPU_TIME_KEY, Float.valueOf(matcher.group(3).trim()));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", key, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(key, cpuTimeData);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static String createCpuName(final int cpuNumber) {
        return "cpu" + cpuNumber;
    }
}
