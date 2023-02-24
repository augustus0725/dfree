package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class Connection {
    private Revision revision;
    private String id;
    private String uri;
    private Permissions permissions;

    // relation (begin)
    private String sourceId;
    private String sourceGroupId;
    private String sourceType;

    private String destinationId;
    private String destinationGroupId;
    private String destinationType;
    // relation (end)

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Component {
        private String id;
        private String parentGroupId;
        private String name;
        private List<String> selectedRelationships;
        private List<String> availableRelationships;
        private Long backPressureObjectThreshold;
        private String backPressureDataSizeThreshold;
        private String flowFileExpiration;
        private String loadBalanceStrategy;
        private String loadBalancePartitionAttribute;
        private String loadBalanceCompression;
        private String loadBalanceStatus;
        // source
        private Pair source;
        // destination
        private Pair destination;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Pair {
        private String id;
        private String type;
        private String groupId;
        private String name;
        private boolean running;
        private String comments;
    }
}
