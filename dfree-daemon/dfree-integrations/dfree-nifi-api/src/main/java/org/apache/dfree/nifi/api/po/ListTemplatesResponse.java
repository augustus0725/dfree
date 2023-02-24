package org.apache.dfree.nifi.api.po;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class ListTemplatesResponse {
    private List<Template> templates;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class Template {
        private String id;
        private Permissions permissions;
        private TemplateContent template;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    public static class TemplateContent {
        private String uri;
        private String groupId;
        private String name;
        private String description;
        private String timestamp;
        @SerializedName("encoding-version")
        private String encodingVersion;
    }
}
