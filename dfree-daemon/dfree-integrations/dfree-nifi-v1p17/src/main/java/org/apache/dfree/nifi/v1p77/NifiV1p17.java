package org.apache.dfree.nifi.v1p77;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.dfree.nifi.api.AbstractDfreeNifi;
import org.apache.dfree.nifi.api.exception.DfreeNifiException;
import org.apache.dfree.nifi.api.po.*;
import org.springframework.http.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NifiV1p17 extends AbstractDfreeNifi {
    public NifiV1p17(String nifiConnectString) {
        super(nifiConnectString);
    }

    private static final String PATH_CREATE_NAMESPACE = "/nifi-api/process-groups/%s/process-groups";
    private static final String PATH_LIST_NAMESPACE = "/nifi-api/flow/process-groups/%s";
    private static final String PATH_DELETE_NAMESPACE = "/nifi-api/process-groups/";
    private static final String PATH_LIST_TEMPLATES = "/nifi-api/flow/templates";
    private static final String PATH_CREATE_TEMPLATE_INSTANCE = "/nifi-api/process-groups/%s/template-instance";
    private static final String PATH_CAT_INSTANCE = "/nifi-api/flow/process-groups/";
    private static final String PATH_CAT_PROCESSOR = "/nifi-api/processors/";
    private static final String PATH_UPDATE_INSTANCE_CONFIG = "/nifi-api/processors/%s";
    private static final String PATH_RUN_STATUS_INSTANCE = "/nifi-api/remote-process-groups/process-group/%s/run-status";
    private static final String PATH_START_STOP_INSTANCE = "/nifi-api/flow/process-groups/%s";

    @Override
    public CreateNamespaceResponse createNamespace(String namespace) {
        if (null != findIdByNamespace(namespace)) {
            DfreeNifiException.throwException("Namespace : " + namespace + " is existed.");
        }
        return createGroup("root", namespace);
    }

    private CreateNamespaceResponse createGroup(String parentGroupId, String group) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<CreateNamespaceRequest> entity = new HttpEntity<>(CreateNamespaceRequest.builder()
                .disconnectedNodeAcknowledged(false)
                .revision(Revision.builder()
                        .version(0L).build())
                .component(
                        CreateNamespaceRequest.Component.builder()
                                .name(group)
                                .build()
                ).build(), headers);

        ResponseEntity<CreateNamespaceResponse> response = restTemplate.exchange(
                nifiConnectString + String.format(PATH_CREATE_NAMESPACE, parentGroupId),
                HttpMethod.POST, entity, CreateNamespaceResponse.class);
        if (response.getStatusCode() == HttpStatus.CREATED
                && null != response.getBody()
                && group.equals(response.getBody().getComponent().getName())) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public ListNamespacesResponse listNamespaces() {
        return listEntries("root");
    }

    private ListNamespacesResponse listEntries(String parent) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ListNamespacesResponse> response = restTemplate.exchange(
                nifiConnectString + String.format(PATH_LIST_NAMESPACE, parent),
                HttpMethod.GET, entity, ListNamespacesResponse.class);
        if (response.getStatusCode() == HttpStatus.OK && null != response.getBody()) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public void deleteNamespace(String namespace) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ListNamespacesResponse.ProcessGroup p = findIdByNamespace(namespace);
        assert p != null;
        restTemplate.exchange(nifiConnectString + PATH_DELETE_NAMESPACE + p.getComponent().getId()
                        + "?version={version}&disconnectedNodeAcknowledged={disconnectedNodeAcknowledged}",
                HttpMethod.DELETE,
                entity,
                DropNamespaceResponse.class,
                ImmutableMap.of(
                        "version", String.valueOf(p.getRevision().getVersion()),
                        "disconnectedNodeAcknowledged", String.valueOf(false)
                ));
    }

    @Override
    public ListTemplatesResponse listTemplates() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<ListTemplatesResponse> response = restTemplate.exchange(
                nifiConnectString + PATH_LIST_TEMPLATES, HttpMethod.GET, entity, ListTemplatesResponse.class);
        if (response.getStatusCode() == HttpStatus.OK && null != response.getBody()) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public CreateTemplateInstanceResponse createTemplateInstance(String namespace, String name, String template) {
        ListNamespacesResponse.ProcessGroup p = findIdByNamespace(namespace);
        String templateId = findIdByTemplate(template);
        assert p != null;
        CreateNamespaceResponse response = createEmptyInstance(p, name);
        assert response != null;
        return loadTemplate(response.getComponent().getId(), templateId);
    }

    @Override
    public CatInstanceResponse catInstance(String namespace, String instance) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String instanceId = findInstanceId(namespace, instance);
        assert instanceId != null;
        ResponseEntity<CatInstanceResponse> response = restTemplate.exchange(
                nifiConnectString + PATH_CAT_INSTANCE + instanceId,
                HttpMethod.GET, entity, CatInstanceResponse.class
        );
        if (response.getStatusCode() == HttpStatus.OK
                && null != response.getBody()
                && null != response.getBody().getProcessGroupFlow()) {
            return response.getBody();
        }
        return null;
    }

    // ComponentConfiguration
    @Override
    public void updateInstance(String namespace, String instance, Map<String, Map<String, String>> properties) {
        CatInstanceResponse instanceResponse = catInstance(namespace, instance);
        if (null == instanceResponse) {
            return;
        }
        Map<String, CreateTemplateInstanceResponse.Processor> compoNameObjectMapping = Maps.newHashMap();

        for (CreateTemplateInstanceResponse.Processor processor :
                instanceResponse.getProcessGroupFlow().getFlow().getProcessors()) {
            compoNameObjectMapping.put(processor.getComponent().getName(), processor);
        }

        List<ComponentConfiguration> processorsToBeModified = Lists.newArrayList();
        // 先获取每个processor的配置, 然后用修改的配置覆盖
        for (Map.Entry<String, Map<String, String>> entry : properties.entrySet()) {
            CreateTemplateInstanceResponse.Processor processor = compoNameObjectMapping.get(entry.getKey());
            if (null != processor) {
                Map<String, String> original = processor.getComponent().getConfig().getProperties();
                // override it
                original.putAll(entry.getValue());
                processorsToBeModified.add(
                        ComponentConfiguration
                                .builder()
                                .disconnectedNodeAcknowledged(false)
                                .revision(processor.getRevision())
                                .component(
                                        ComponentConfiguration.Component.builder()
                                                .id(processor.getId())
                                                .name(processor.getComponent().getName())
                                                .state(processor.getComponent().getState())
                                                .config(processor.getComponent().getConfig()).build()
                                ).build()
                );
            }
        }
        // do modify
        updateProcessorConfiguration(processorsToBeModified);
    }

    @Override
    public void scheduleInstance(String namespace, String instance, ScheduleStrategy scheduleStrategy) {
        List<ComponentConfiguration> processorsToBeModified = Lists.newArrayList();
        for (CreateTemplateInstanceResponse.Processor processor : findHeadProcessors(namespace, instance)) {
            Config config = processor.getComponent().getConfig();
            // set schedule parameters
            config.setSchedulingStrategy(scheduleStrategy.getSchedulingStrategy());
            config.setSchedulingPeriod(scheduleStrategy.getSchedulingPeriod());
            config.setProperties(Collections.emptyMap());

            processorsToBeModified.add(
                    ComponentConfiguration
                            .builder()
                            .disconnectedNodeAcknowledged(false)
                            .revision(processor.getRevision())
                            .component(
                                    ComponentConfiguration.Component.builder()
                                            .id(processor.getId())
                                            .name(processor.getComponent().getName())
                                            .state(processor.getComponent().getState())
                                            .config(processor.getComponent().getConfig()).build()
                            ).build()
            );
        }
        updateProcessorConfiguration(processorsToBeModified);
    }

    private List<CreateTemplateInstanceResponse.Processor> findHeadProcessors(String namespace, String instance) {
        CatInstanceResponse instanceResponse = catInstance(namespace, instance);
        List<CreateTemplateInstanceResponse.Processor> headProcessors = Lists.newArrayList();
        if (null != instanceResponse) {
            for (CreateTemplateInstanceResponse.Processor processor :
                    instanceResponse.getProcessGroupFlow().getFlow().getProcessors()) {
                if ("INPUT_FORBIDDEN".equals(processor.getComponent().getInputRequirement())) {
                    headProcessors.add(processor);
                }
            }
        }
        return headProcessors;
    }

    @Override
    public void startInstance(String namespace, String instance) {
        updateInstanceState(namespace, instance, "TRANSMITTING", "RUNNING");
    }

    @Override
    public void stopInstance(String namespace, String instance) {
        updateInstanceState(namespace, instance, "STOPPED", "STOPPED");
    }

    @Override
    public List<DfreeInstance> listInstances(String namespace) {
        List<DfreeInstance> instances = Lists.newArrayList();
        String namespaceId;

        ListNamespacesResponse.ProcessGroup group = findIdByNamespace(namespace);
        if (null != group) {
            namespaceId = group.getComponent().getId();
            ListNamespacesResponse response = listEntries(namespaceId);
            if (null != response) {
                for (ListNamespacesResponse.ProcessGroup g : response.getProcessGroupFlow().getFlow().getProcessGroups()) {
                    instances.add(DfreeInstance.builder().name(g.getComponent().getName()).status(
                            g.getRunningCount() > 0 ? "running" : "stopped"
                    ).runningProcessors(g.getRunningCount()).stoppedProcessors(g.getStoppedCount()).build());
                }
            }
        }
        return instances;
    }

    private void updateInstanceState(String namespace, String instance, String state, String startStopState) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<UpdateStateRequest> entity = new HttpEntity<>(
                UpdateStateRequest.builder().state(state).disconnectedNodeAcknowledged(false).build(), headers);

        String instanceId = findInstanceId(namespace, instance);
        ResponseEntity<UpdateStateResponse> response = restTemplate.exchange(
                nifiConnectString + String.format(PATH_RUN_STATUS_INSTANCE, instanceId),
                HttpMethod.PUT, entity, UpdateStateResponse.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DfreeNifiException(String.format("Fail to update %s:%s state : %s", namespace, instance, state));
        }

        HttpEntity<StartStopRequest> startStopRequest = new HttpEntity<>(
                StartStopRequest.builder().state(startStopState).id(instanceId).disconnectedNodeAcknowledged(false).build(), headers);
        ResponseEntity<StartStopRequestResponse> startStopResponse = restTemplate.exchange(
                nifiConnectString + String.format(PATH_START_STOP_INSTANCE, instanceId),
                HttpMethod.PUT, startStopRequest, StartStopRequestResponse.class);
        if (startStopResponse.getStatusCode() != HttpStatus.OK && startStopResponse.getBody() != null
                && !startStopState.equals(startStopResponse.getBody().getState())) {
            throw new DfreeNifiException(String.format("Fail to update %s:%s state : %s", namespace, instance, state));
        }
    }

    private void updateProcessorConfiguration(List<ComponentConfiguration> processorsToBeModified) {
        for (ComponentConfiguration configuration : processorsToBeModified) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
            headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
            HttpEntity<ComponentConfiguration> entity = new HttpEntity<>(configuration, headers);

            ResponseEntity<UpdateProcessorConfigurationResponse> response = restTemplate.exchange(
                    nifiConnectString + String.format(PATH_UPDATE_INSTANCE_CONFIG, configuration.getComponent().getId()),
                    HttpMethod.PUT, entity, UpdateProcessorConfigurationResponse.class);
            if (!(response.getStatusCode() == HttpStatus.OK
                    && null != response.getBody()
                    && response.getBody().getRevision().getVersion() > configuration.getRevision().getVersion())) {
                throw new DfreeNifiException("Fail to update config : " + configuration);
            }
        }
    }

    @Override
    public CatProcessorResponse catProcessor(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CatProcessorResponse> response = restTemplate.exchange(
                nifiConnectString + PATH_CAT_PROCESSOR + id,
                HttpMethod.GET, entity, CatProcessorResponse.class);
        if (response.getStatusCode() == HttpStatus.OK && null != response.getBody()) {
            return response.getBody();
        }
        return null;
    }

    private String findInstanceId(String namespace, String instance) {
        ListNamespacesResponse.ProcessGroup p = findIdByNamespace(namespace);
        assert null != p && null != p.getComponent().getId();
        ListNamespacesResponse response = listEntries(p.getComponent().getId());

        String returnValue = null;
        if (response != null && !response.getProcessGroupFlow().getFlow().getProcessGroups().isEmpty()) {
            for (ListNamespacesResponse.ProcessGroup v : response.getProcessGroupFlow().getFlow().getProcessGroups()) {
                if (instance.equals(v.getComponent().getName())) {
                    returnValue = v.getComponent().getId();
                }
            }
        }

        return returnValue;
    }

    private String findIdByTemplate(String template) {
        ListTemplatesResponse response = listTemplates();
        if (response != null && !response.getTemplates().isEmpty()) {
            for (ListTemplatesResponse.Template v : response.getTemplates()
            ) {
                if (template.equals(v.getTemplate().getName())) {
                    return v.getId();
                }
            }
        }
        return null;
    }

    private CreateTemplateInstanceResponse loadTemplate(String groupId, String templateId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        HttpEntity<CreateTemplateInstanceRequest> entity = new HttpEntity<>(
                CreateTemplateInstanceRequest.builder()
                        .templateId(templateId)
                        .disconnectedNodeAcknowledged(false)
                        .originX(500L).originY(300L)
                        .build(), headers);

        ResponseEntity<CreateTemplateInstanceResponse> response = restTemplate.exchange(
                nifiConnectString + String.format(PATH_CREATE_TEMPLATE_INSTANCE, groupId),
                HttpMethod.POST, entity, CreateTemplateInstanceResponse.class);
        if (response.getStatusCode() == HttpStatus.CREATED
                && null != response.getBody()) {
            return response.getBody();
        }
        return null;
    }

    private CreateNamespaceResponse createEmptyInstance(ListNamespacesResponse.ProcessGroup p, String name) {
        assert p != null;
        return createGroup(p.getComponent().getId(), name);
    }

    private ListNamespacesResponse.ProcessGroup findIdByNamespace(String namespace) {
        ListNamespacesResponse response = listNamespaces();
        if (response.getProcessGroupFlow() != null
                && response.getProcessGroupFlow().getFlow() != null
                && response.getProcessGroupFlow().getFlow().getProcessGroups() != null &&
                !response.getProcessGroupFlow().getFlow().getProcessGroups().isEmpty()) {
            for (ListNamespacesResponse.ProcessGroup p : response.getProcessGroupFlow().getFlow().getProcessGroups()) {
                if (namespace.equals(p.getComponent().getName())) {
                    return p;
                }
            }
        }
        return null;
    }
}
