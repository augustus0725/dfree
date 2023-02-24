package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class CreateNamespaceResponse {
    private Revision revision;
    private Component component;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Component {
        private String id;
        private String parentGroupId;
        private String name;
    }
}
