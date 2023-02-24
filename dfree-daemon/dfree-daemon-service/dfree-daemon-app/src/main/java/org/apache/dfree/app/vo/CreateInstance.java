package org.apache.dfree.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.dfree.nifi.api.po.ScheduleStrategy;

import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
public class CreateInstance {
    private String namespace;
    private String instance;
    private String template;

    private Map<String, Map<String, String>> properties;
    private ScheduleStrategy scheduleStrategy;
}
