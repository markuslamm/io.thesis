package io.thesis.collector.flink.jmx;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

public abstract class AbstractJmxIT {

    protected MBeanServerConnection mBeanServerConnection() throws IOException {
        final JMXServiceURL jmxServiceURL = new JMXServiceURL(getJmxUrl());
        final JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);
        return jmxConnector.getMBeanServerConnection();
    }

    protected abstract String getJmxUrl();
}
