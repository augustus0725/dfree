package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class CatInstanceResponse extends CreateTemplateInstanceResponse{
    private Permissions permissions;
    private ProcessGroupFlow processGroupFlow;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class ProcessGroupFlow {
        private String id;
        private String uri;
        private String parentGroupId;
        private CreateTemplateInstanceResponse.Flow flow;
    }
}
