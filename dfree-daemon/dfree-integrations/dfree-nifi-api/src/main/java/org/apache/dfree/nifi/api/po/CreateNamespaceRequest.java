package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class CreateNamespaceRequest {
    private boolean disconnectedNodeAcknowledged;
    private Component component;
    private Revision revision;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Component {
        private String name;
    }
}
