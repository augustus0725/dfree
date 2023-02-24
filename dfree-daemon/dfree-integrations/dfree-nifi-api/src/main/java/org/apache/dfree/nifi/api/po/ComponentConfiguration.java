package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ComponentConfiguration {
    private Component component;
    private Revision revision;
    private boolean disconnectedNodeAcknowledged;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Component {
        private String id;
        private String name;
        private Config config;
        private String state;
    }
}
