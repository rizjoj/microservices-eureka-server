package com.riz.microservices.eureka.healthcheck;

import com.google.common.collect.ImmutableMap;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Update #getStatus() to do your detailed application check (testing for databases, external services, queues, etc.)
 * to represent your health check by indicating all its components are also functional (as opposed to a simple
 * application-is-running check).
 */
public class EurekaHealthCheckHandler implements HealthCheckHandler, ApplicationContextAware, InitializingBean {

    public final static ImmutableMap<Status, InstanceInfo.InstanceStatus> healthStatuses =
            new ImmutableMap.Builder<Status, InstanceInfo.InstanceStatus>()
                    .put(Status.UNKNOWN,        InstanceInfo.InstanceStatus.UNKNOWN)
                    .put(Status.OUT_OF_SERVICE, InstanceInfo.InstanceStatus.OUT_OF_SERVICE)
                    .put(Status.DOWN,           InstanceInfo.InstanceStatus.DOWN)
                    .put(Status.UP,             InstanceInfo.InstanceStatus.UP)
                    .build();

    private final CompositeHealthIndicator healthIndicator;

    private ApplicationContext applicationContext;

    public EurekaHealthCheckHandler(HealthAggregator healthAggregator) {
        Assert.notNull(healthAggregator, "HealthAggregator must not be null");
        this.healthIndicator = new CompositeHealthIndicator(healthAggregator);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Map<String, HealthIndicator> healthIndicators = applicationContext.getBeansOfType(HealthIndicator.class);
        for (Map.Entry<String, HealthIndicator> entry : healthIndicators.entrySet()) {
            healthIndicator.addHealthIndicator(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public InstanceInfo.InstanceStatus getStatus(InstanceInfo.InstanceStatus instanceStatus) {
        Status status = healthIndicator.health().getStatus();
        return healthStatuses.containsKey(status) ? healthStatuses.get(status) : InstanceInfo.InstanceStatus.UNKNOWN;
    }
}
