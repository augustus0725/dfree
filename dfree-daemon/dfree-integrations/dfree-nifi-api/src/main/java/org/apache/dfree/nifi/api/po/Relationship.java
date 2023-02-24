package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class Relationship {
    private String name;
    private String description;
    private boolean autoTerminate;
    private boolean retry;
}
