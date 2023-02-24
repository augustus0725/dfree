package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class ListNamespacesResponse {
    private ProcessGroupFlow processGroupFlow;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class ProcessGroupFlow {
        private String id;
        private Flow flow;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Flow {
        private List<ProcessGroup> processGroups;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class ProcessGroup {
        private Component component;
        private Status status;
        private Permissions permissions;
        private Revision revision;
        private Long runningCount;
        private Long stoppedCount;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Component {
        private String id;
        private String parentGroupId;
        private String name;
        private String comments;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Status {
        private String statsLastRefreshed;
    }
}
