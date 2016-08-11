//package io.thesis.collector.jvm;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import javax.management.MBeanServerConnection;
//
//@Profile("default-jvm-jmx")
//@Configuration
//public class DefaultJvmCollectorConfig {
//
//    @Bean
//    DefaultJvmCollector defaultJvmCollector(final MBeanServerConnection mBeanServerConnection) {
//        return new DefaultJvmCollector(mBeanServerConnection);
//    }
//}
