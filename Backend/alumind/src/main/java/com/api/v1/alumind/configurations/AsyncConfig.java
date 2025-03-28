package com.api.v1.alumind.configurations;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class AsyncConfig {

    private final ThreadPoolTaskExecutor taskExecutor = createTaskExecutor(2, 2, 10, "Thread_Email_Send-");

    private ThreadPoolTaskExecutor createTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, String threadPrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadPrefix);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(1);
        executor.initialize();
        return executor;
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        return taskExecutor;
    }

    @PreDestroy
    public void destroyExecutors() {
        taskExecutor.shutdown();
    }
}
