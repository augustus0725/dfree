package org.apache.dfree.nifi.api.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class Permissions {
    private boolean canRead;
    private boolean canWrite;
}
