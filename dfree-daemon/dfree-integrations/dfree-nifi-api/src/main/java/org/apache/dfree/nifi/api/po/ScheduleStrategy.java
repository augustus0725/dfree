package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ScheduleStrategy {
    public static class SchedulingStrategy {
        public static String CRON_DRIVEN = "CRON_DRIVEN";
        public static String TIMER_DRIVEN = "TIMER_DRIVEN";

    }

    private String schedulingPeriod;
    private String schedulingStrategy;
}
