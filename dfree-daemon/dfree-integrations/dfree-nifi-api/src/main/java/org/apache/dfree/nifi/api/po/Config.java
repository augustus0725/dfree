package org.apache.dfree.nifi.api.po;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
public class Config {
    private Map<String, String> properties;
    private String schedulingPeriod;
    private String schedulingStrategy;
    private String executionNode;
    private String penaltyDuration;
    private String yieldDuration;
    private String bulletinLevel;
    private Long runDurationMillis;
    private Long concurrentlySchedulableTaskCount;
    private String comments;
    private boolean lossTolerant;

    private DefaultConcurrentTasks defaultConcurrentTasks;
    private DefaultSchedulingPeriod defaultSchedulingPeriod;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class DefaultConcurrentTasks {
        @SerializedName("TIMER_DRIVEN")
        private String timerDriven;
        @SerializedName("EVENT_DRIVEN")
        private String eventDriven;
        @SerializedName("CRON_DRIVEN")
        private String cronDriven;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class DefaultSchedulingPeriod {
        @SerializedName("TIMER_DRIVEN")
        private String timerDriven;
        @SerializedName("CRON_DRIVEN")
        private String cronDriven;
    }

    private Long retryCount;
    private String backoffMechanism;
    private String maxBackoffPeriod;
}
