package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CatProcessorResponse {
    private Revision revision;
    private String id;
    private String uri;
    private Permissions permissions;
    private Component component;
    private String inputRequirement;
    private Status status;

    public static class Component {
        private String id;
        private String parentGroupId;
        private String name;
        private String type;
        private Bundle bundle;
        private String state;
        private List<Relationship> relationships;

        private boolean supportsParallelProcessing;
        private boolean supportsEventDriven;
        private boolean supportsBatching;
        private boolean supportsSensitiveDynamicProperties;
        private boolean persistsState;
        private boolean restricted;
        private boolean deprecated;
        private boolean executionNodeRestricted;
        private boolean multipleVersionsAvailable;
        private String inputRequirement;
        private Config config;
        private String validationStatus;
        private boolean extensionMissing;
    }
}
