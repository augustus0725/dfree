package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class DfreeInstance {
    private String name;
    private String status;
    private long runningProcessors;
    private long stoppedProcessors;
}
