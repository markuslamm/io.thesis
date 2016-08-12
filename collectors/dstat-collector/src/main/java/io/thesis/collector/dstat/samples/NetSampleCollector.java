package io.thesis.collector.dstat.samples;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.thesis.collector.dstat.AbstractDstatSampleCollector;
import io.thesis.collector.dstat.DStatCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.thesis.collector.dstat.util.ConversionUtil.convertKilobytes;
import static java.lang.String.format;

/**
 * Parses Dstat output given with the parameters "--net", "--socket", "--tcp", "--udp".
 */
public final class NetSampleCollector extends AbstractDstatSampleCollector {

    public static final String SAMPLE_KEY = "net";

    private static final Logger LOG = LoggerFactory.getLogger(NetSampleCollector.class);
    private static final String NET_LISTEN_KEY = "listen";
    private static final String NET_SEND_KEY = "send";
    private static final String NET_RCV_KEY = "received";
    private static final String NET_TRAFFIC_KEY = "traffic";
    private static final String NET_NAME_KEY = "name";
    private static final String NET_SOCKETS_KEY = "sockets";
    private static final String NET_SOCKETS_TOTAL_KEY = "total";
    private static final String NET_SOCKETS_TCP_KEY = "tcp";
    private static final String NET_SOCKETS_UDP_KEY = "udp";
    private static final String NET_SOCKETS_RAW_KEY = "raw";
    private static final String NET_SOCKETS_IP_FRAGMENTS_KEY = "ipFragments";
    private static final String NET_TCP_SOCKETS_KEY = "tcp-sockets";
    private static final String NET_TCP_SOCKETS_EST_KEY = "established";
    private static final String NET_TCP_SOCKETS_SYN_KEY = "syn";
    private static final String NET_TCP_SOCKETS_TIME_WAIT_KEY = "timeWait";
    private static final String NET_TCP_SOCKETS_CLOSE_KEY = "close";
    private static final String NET_UDP_KEY = "udp";
    private static final String NET_UDP_ACTIVE_KEY = "active";

    private static final Pattern NET_INTERFACE_NAME_PATTERN;
    private static final Pattern NET_STATS_PATTERN;
    private static final Pattern NET_SOCKETS_PATTERN;
    private static final Pattern NET_TCP_SOCKETS_PATTERN;
    private static final Pattern NET_UDP_PATTERN;

    static {
        NET_INTERFACE_NAME_PATTERN = Pattern.compile("(net/)([a-zA-Z0-9-]+)");

        NET_STATS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?([kBMG]?))(\\s*)" +
                "(\\d+(\\.\\d+)?([kBMG]?))");

        NET_SOCKETS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        NET_TCP_SOCKETS_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");

        NET_UDP_PATTERN = Pattern.compile("" +
                "(\\d+(\\.\\d+)?)(\\s*)" +
                "(\\d+(\\.\\d+)?)");
    }

    @Override
    public Map<String, Object> applyData(final String[] dstatLines) {
        final String[] columns = dstatLines[2].split("\\|");
        final Map<String, Object> netDataMap = Maps.newLinkedHashMap();
        final List<String> networkInterfaceNames = getNetworkInterfaceNames(dstatLines[0]);
        netDataMap.putAll(parseNetInterfacesData(columns[8], networkInterfaceNames));
        netDataMap.putAll(parseNetSockets(columns[9]));
        netDataMap.putAll(parseNetTcpSockets(columns[10]));
        netDataMap.putAll(parseNetUdpSockets(columns[11]));
        return netDataMap;
    }

    @Override
    protected Map<String, Object> createResultMap(final Map<String, Object> data) {
        final Map<String, Object> netResultMap = Maps.newLinkedHashMap();
        netResultMap.put(SAMPLE_KEY, data);
        return netResultMap;
    }

    private List<String> getNetworkInterfaceNames(final String firstLine) {
        final Matcher matcher = NET_INTERFACE_NAME_PATTERN.matcher(firstLine.trim());
        final List<String> result = Lists.newLinkedList();
        while (matcher.find()) {
            result.add(matcher.group(2));
        }
        if (result.isEmpty()) {
            LOG.warn("Unable to parse '{}': regex mismatch", SAMPLE_KEY + " interface names");
        }
        return result;
    }

    private Map<String, List<Map<String, Object>>> parseNetInterfacesData(final String inputData, final List<String> networkInterfaceNames) {
        final List<Map<String, Object>> netTrafficList = Lists.newLinkedList();
        if (inputData.contains(":")) {
            final String[] netInterfaceData = inputData.split(":");
            if (netInterfaceData.length != networkInterfaceNames.size()) {
                throw new DStatCollectorException(format("Invalid data, network names length [%s] differs from data length [%s]",
                        networkInterfaceNames.size(), netInterfaceData.length));
            }
            for (int i = 0; i < netInterfaceData.length; i++) {
                netTrafficList.add(parseNetTraffic(netInterfaceData[i], networkInterfaceNames.get(i)));
            }
        } else {
            netTrafficList.add(parseNetTraffic(inputData, networkInterfaceNames.get(0)));
        }
        final Map<String, List<Map<String, Object>>> netStatMap = Maps.newLinkedHashMap();
        netStatMap.put(NET_TRAFFIC_KEY, netTrafficList);
        return netStatMap;
    }

    private static Map<String, Object> parseNetTraffic(final String rawData, final String interfaceName) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> result = Maps.newLinkedHashMap();
            final Matcher matcher = NET_STATS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", SAMPLE_KEY + " send/rcv", raw);
            }
            try {
                result.put(NET_NAME_KEY, interfaceName);
                result.put(NET_SEND_KEY, convertKilobytes(matcher.group(1)));
                result.put(NET_RCV_KEY, convertKilobytes(matcher.group(5)));
            } catch (NumberFormatException ex) {
                LOG.warn("Unable to parse '{}': {}", SAMPLE_KEY + " send/rcv", ex.getMessage());
            }
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseNetSockets(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> netSocketsDataMap = Maps.newLinkedHashMap();
            final Matcher matcher = NET_SOCKETS_PATTERN.matcher(raw.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", NET_SOCKETS_KEY, raw);
            } else {
                try {
                    netSocketsDataMap.put(NET_SOCKETS_TOTAL_KEY, Float.valueOf(matcher.group(1)));
                    netSocketsDataMap.put(NET_SOCKETS_TCP_KEY, Float.valueOf(matcher.group(4)));
                    netSocketsDataMap.put(NET_SOCKETS_UDP_KEY, Float.valueOf(matcher.group(7)));
                    netSocketsDataMap.put(NET_SOCKETS_RAW_KEY, Float.valueOf(matcher.group(10)));
                    netSocketsDataMap.put(NET_SOCKETS_IP_FRAGMENTS_KEY, Float.valueOf(matcher.group(13)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': regex mismatch", NET_SOCKETS_KEY);
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(NET_SOCKETS_KEY, netSocketsDataMap);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseNetTcpSockets(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> netTcpSocketsDataMap = Maps.newLinkedHashMap();
            final Matcher matcher = NET_TCP_SOCKETS_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", NET_TCP_SOCKETS_KEY, rawData);
            } else {
                try {
                    netTcpSocketsDataMap.put(NET_LISTEN_KEY, Float.valueOf(matcher.group(1)));
                    netTcpSocketsDataMap.put(NET_TCP_SOCKETS_EST_KEY, Float.valueOf(matcher.group(4)));
                    netTcpSocketsDataMap.put(NET_TCP_SOCKETS_SYN_KEY, Float.valueOf(matcher.group(7)));
                    netTcpSocketsDataMap.put(NET_TCP_SOCKETS_TIME_WAIT_KEY, Float.valueOf(matcher.group(10)));
                    netTcpSocketsDataMap.put(NET_TCP_SOCKETS_CLOSE_KEY, Float.valueOf(matcher.group(13)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': regex mismatch", NET_TCP_SOCKETS_KEY);
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(NET_TCP_SOCKETS_KEY, netTcpSocketsDataMap);
            return result;
        }).orElse(Maps.newHashMap());
    }

    private static Map<String, Object> parseNetUdpSockets(final String rawData) {
        return Optional.ofNullable(rawData).map(raw -> {
            final Map<String, Object> netUdpDataMap = Maps.newLinkedHashMap();
            final Matcher matcher = NET_UDP_PATTERN.matcher(rawData.trim());
            if (!matcher.matches()) {
                LOG.warn("Unable to parse '{}': regex mismatch: {}", NET_UDP_KEY, rawData);
            } else {
                try {
                    netUdpDataMap.put(NET_LISTEN_KEY, Float.valueOf(matcher.group(1)));
                    netUdpDataMap.put(NET_UDP_ACTIVE_KEY, Float.valueOf(matcher.group(4)));
                } catch (NumberFormatException ex) {
                    LOG.warn("Unable to parse '{}': {}", NET_UDP_KEY, ex.getMessage());
                }
            }
            final Map<String, Object> result = Maps.newLinkedHashMap();
            result.put(NET_UDP_KEY, netUdpDataMap);
            return result;
        }).orElse(Maps.newHashMap());
    }
}
