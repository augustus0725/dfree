package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class StartStopRequest {
    private String id;
    private String state;
    private boolean disconnectedNodeAcknowledged;
}
