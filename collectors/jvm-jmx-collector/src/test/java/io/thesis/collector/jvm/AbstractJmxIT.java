package io.thesis.collector.jvm;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

public abstract class AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";

    protected MBeanServerConnection mBeanServerConnection() throws IOException {
        final JMXServiceURL jmxServiceURL = new JMXServiceURL(JMX_URL);
        final JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);
        return jmxConnector.getMBeanServerConnection();
    }
}
