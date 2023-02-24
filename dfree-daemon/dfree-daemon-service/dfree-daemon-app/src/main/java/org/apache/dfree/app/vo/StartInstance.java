package org.apache.dfree.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class StartInstance {
    private String namespace;
    private String instance;
}
