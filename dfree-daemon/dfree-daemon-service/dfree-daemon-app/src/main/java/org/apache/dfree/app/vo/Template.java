package org.apache.dfree.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class Template {
    private String name;
    private String description;
    private String timestamp;
}
