package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class CreateTemplateInstanceRequest {
    private String templateId;
    private boolean disconnectedNodeAcknowledged;
    private Long originX;
    private Long originY;
}
