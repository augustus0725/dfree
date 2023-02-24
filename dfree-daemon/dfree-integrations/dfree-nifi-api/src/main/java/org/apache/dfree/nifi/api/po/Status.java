package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class Status {
    private String id;
    private String groupId;
    private String name;
    private String runStatus;
    private String statsLastRefreshed;
    private AggregateSnapshot aggregateSnapshot;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class AggregateSnapshot {
        private String id;
        private String groupId;
        private String name;
        private String type;
        private String runStatus;
        private String executionNode;
        private Long bytesRead;
        private Long bytesWritten;
        private String read;
        private String written;
        private Long flowFilesIn;
        private Long bytesIn;
        private String input;
        private Long flowFilesOut;
        private Long bytesOut;
        private String output;
        private Long taskCount;
        private Long tasksDurationNanos;
        private String tasks;
        private String tasksDuration;
        private Long activeThreadCount;
        private Long terminatedThreadCount;
    }
}
