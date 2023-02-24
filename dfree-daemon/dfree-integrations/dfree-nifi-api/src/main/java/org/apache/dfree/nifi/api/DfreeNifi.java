package org.apache.dfree.nifi.api;

import org.apache.dfree.nifi.api.po.*;

import java.util.List;
import java.util.Map;

public interface DfreeNifi {
    CreateNamespaceResponse createNamespace(String namespace);
    ListNamespacesResponse listNamespaces();
    void deleteNamespace(String namespace);
    ListTemplatesResponse listTemplates();
    CreateTemplateInstanceResponse createTemplateInstance(String namespace, String name, String template);
    CatProcessorResponse catProcessor(String id);
    CatInstanceResponse catInstance(String namespace, String instance);
    void updateInstance(String namespace, String instance, Map<String, Map<String, String>> properties);
    void scheduleInstance(String namespace, String instance, ScheduleStrategy scheduleStrategy);
    void startInstance(String namespace, String instance);
    void stopInstance(String namespace, String instance);
    List<DfreeInstance> listInstances(String namespace);
}
