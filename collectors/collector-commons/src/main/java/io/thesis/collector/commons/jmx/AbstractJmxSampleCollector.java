package io.thesis.collector.commons.jmx;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.thesis.collector.commons.SampleCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Base class for all {@code SampleCollector} implementations.
 */
public abstract class AbstractJmxSampleCollector implements SampleCollector {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJmxSampleCollector.class);

    private final MBeanServerConnection mBeanServerConnection;

    protected AbstractJmxSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        this.mBeanServerConnection = requireNonNull(mBeanServerConnection);
    }

    /**
     * Makes mBeanServerConnection accessible for subclasses.
     *
     * @return the {@code MBeanServerConnection} the collector gathers data from.
     */
    protected MBeanServerConnection mBeanServerConnection() {
        return mBeanServerConnection;
    }

    protected static ObjectName convertStringToObjectName(final String objectName) {
        try {
            return new ObjectName(objectName);
        } catch (MalformedObjectNameException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to convert ObjectName '%s': %s", objectName, ex.getMessage()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }

    protected Set<ObjectName> queryObjectNames(final ObjectName queryName) {
        try {
            return mBeanServerConnection().queryNames(queryName, null);
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to query ObjectName '%s': %s", queryName.getCanonicalName(),
                    ex.getMessage()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }

    protected MBeanInfo getMBeanInfo(final ObjectName objectName) {
        try {
            return mBeanServerConnection().getMBeanInfo(objectName);
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException | IOException ex) {
            final JmxCollectorException exception =
                    new JmxCollectorException(format("Unable to get MBeanInfo '%s': %s, cause: %s",
                            objectName.getCanonicalName(), ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }

    protected Object getAttributeValue(final ObjectName objectName, final String attribute) {
        try {
            return mBeanServerConnection().getAttribute(objectName, attribute);
        } catch (MBeanException | IOException | ReflectionException | InstanceNotFoundException |
                AttributeNotFoundException ex) {
            final JmxCollectorException exception =
                    new JmxCollectorException(format("Unable to get attribute '%s' from '%s': %s, cause: %s",
                            attribute, objectName.getCanonicalName(), ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }

    protected Map<String, Object> parseAllAttributes(final ObjectName objectName) {
        final Map<String, Object> dataMap = Maps.newLinkedHashMap();
        final MBeanInfo mBeanInfo = getMBeanInfo(objectName);
        final MBeanAttributeInfo[] attributeInfos = mBeanInfo.getAttributes();

        for (final MBeanAttributeInfo attributeInfo : attributeInfos) {
            final Object attributeValue = getAttributeValue(objectName, attributeInfo.getName());
            if (attributeValue instanceof Number || attributeValue instanceof String
                    || attributeValue instanceof Boolean || attributeValue instanceof TimeUnit) {
                dataMap.put(attributeInfo.getName(), attributeValue);
            }
        }
        return dataMap;
    }

    protected ObjectName getSingleQueryObject(final String queryString) {
        final ObjectName queryObjectName = convertStringToObjectName(queryString);
        final List<ObjectName> objectNames = Lists.newArrayList(queryObjectNames(queryObjectName));
        if (objectNames.size() != 1) {
            throw new JmxCollectorException(format("The implementation assumes 1 object, but was: %d", objectNames.size()));
        }
        return objectNames.get(0);
    }
}
