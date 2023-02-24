package org.apache.dfree.app.controller;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.dfree.app.vo.*;
import org.apache.dfree.commons.annotation.Loggable;
import org.apache.dfree.commons.standard.RestResponse;
import org.apache.dfree.nifi.api.DfreeNifi;
import org.apache.dfree.nifi.api.exception.DfreeNifiException;
import org.apache.dfree.nifi.api.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor__ = {@Autowired})
public class NifiController {
    private final DfreeNifi dfreeNifi;

    @GetMapping("/namespaces")
    @Loggable
    public RestResponse<List<String>> listNamespaces() {
        ListNamespacesResponse response = dfreeNifi.listNamespaces();
        List<String> result = Lists.newArrayList();

        response.getProcessGroupFlow().getFlow().getProcessGroups().forEach(p -> {
            result.add(p.getComponent().getName());
        });
        return RestResponse.ok(result);
    }

    @PutMapping("/namespace")
    @Loggable
    public RestResponse<Map<Object, Object>> createNamespace(@RequestBody CreateNamespace namespace) {
        CreateNamespaceResponse response = dfreeNifi.createNamespace(namespace.getNamespace());

        if (response == null) {
            throw new DfreeNifiException("Fail to create namespace " + namespace.getNamespace());
        }
        return RestResponse.ok(Collections.emptyMap());
    }

    @DeleteMapping("/namespace")
    @Loggable
    public RestResponse<Map<Object, Object>> deleteNamespace(@RequestBody DeleteNamespace namespace) {
        dfreeNifi.deleteNamespace(namespace.getNamespace());
        return RestResponse.ok(Collections.emptyMap());
    }

    @GetMapping("/templates")
    @Loggable
    public RestResponse<List<Template>> listTemplates() {
        ListTemplatesResponse templates = dfreeNifi.listTemplates();
        List<Template> result = Lists.newArrayList();
        if (templates == null) {
            throw new DfreeNifiException("Fail to get templates.");
        }
        templates.getTemplates().forEach(template -> {
            result.add(
                    Template.builder()
                            .name(template.getTemplate().getName())
                            .description(template.getTemplate().getDescription())
                            .timestamp(template.getTemplate().getTimestamp()).build()
            );
        });
        return RestResponse.ok(result);
    }

    @PutMapping("/instance")
    @Loggable
    public RestResponse<String> createTemplateInstance(@RequestBody CreateInstance createInstance) {
        CreateTemplateInstanceResponse response = dfreeNifi.createTemplateInstance(createInstance.getNamespace(),
                createInstance.getInstance(), createInstance.getTemplate());
        if (response == null) {
            throw new DfreeNifiException(String.format("Fail to create instance: %s from template: %s",
                    createInstance.getInstance(), createInstance.getTemplate()));
        }
        dfreeNifi.updateInstance(createInstance.getNamespace(), createInstance.getInstance(), createInstance.getProperties());
        dfreeNifi.scheduleInstance(createInstance.getNamespace(), createInstance.getInstance(), createInstance.getScheduleStrategy());
        return RestResponse.ok(String.valueOf(response.getFlow().getProcessors().size()));
    }

    @GetMapping("/instances")
    @Loggable
    public RestResponse<List<DfreeInstance>> listInstances(@RequestParam String namespace) {
        List<DfreeInstance> instances = dfreeNifi.listInstances(namespace);
        return RestResponse.ok(instances);
    }

    @PutMapping("/instance/start")
    @Loggable
    public RestResponse<Map<Object, Object>> startInstance(@RequestBody StartInstance startInstance) {
        dfreeNifi.startInstance(startInstance.getNamespace(), startInstance.getInstance());
        return RestResponse.ok(Collections.emptyMap());
    }

    @PutMapping("/instance/stop")
    @Loggable
    public RestResponse<Map<Object, Object>> stopInstance(@RequestBody StopInstance stopInstance) {
        dfreeNifi.stopInstance(stopInstance.getNamespace(), stopInstance.getInstance());
        return RestResponse.ok(Collections.emptyMap());
    }
}
