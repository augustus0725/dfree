package org.apache.dfree.nifi.v1p77;

import com.google.common.collect.ImmutableMap;
import org.apache.dfree.nifi.api.DfreeNifi;
import org.apache.dfree.nifi.api.po.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class NifiV1p17Test {
    private DfreeNifi createDfreeNifiForTest() {
        return new NifiV1p17("http://192.168.0.16:8022");
    }

    @org.junit.Test
    public void createNamespace() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        dfreeNifi.createNamespace("ods");
    }

    @Test
    public void listNamespaces() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        ListNamespacesResponse response = dfreeNifi.listNamespaces();
        assertFalse(response.getProcessGroupFlow().getFlow().getProcessGroups().isEmpty());
    }

    @Test
    public void deleteNamespace() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        dfreeNifi.deleteNamespace("ods");
    }

    @Test
    public void listTemplates() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        ListTemplatesResponse response = dfreeNifi.listTemplates();
        assertFalse(response.getTemplates().isEmpty());
    }

    @Test
    public void createTemplateInstance() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        CreateTemplateInstanceResponse response = dfreeNifi.createTemplateInstance("ods", "instance02", "LogFile");
        assertNotNull(response);
    }

    @Test
    public void catInstance() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        CatInstanceResponse response = dfreeNifi.catInstance("ods", "instance01");
        assertNotNull(response);
    }

    @Test
    public void catProcessor() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        CatProcessorResponse response = dfreeNifi.catProcessor("a392d897-507d-300a-840b-3321a9c28ad6");
        assertNotNull(response);
    }

    @Test
    public void updateInstanceConfigurations() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        dfreeNifi.updateInstance("ods", "instance01",
                ImmutableMap.of("dfree_getfile_v01", ImmutableMap.of("Input Directory", "/opt/getfile/001")));
    }

    @Test
    public void startInstance() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        dfreeNifi.startInstance("ods", "instance01");
    }

    @Test
    public void stopInstance() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        dfreeNifi.stopInstance("ods", "instance01");
    }

    @Test
    public void scheduleInstance() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        dfreeNifi.scheduleInstance("ods", "instance01",
                ScheduleStrategy.builder()
                        .schedulingStrategy(ScheduleStrategy.SchedulingStrategy.CRON_DRIVEN)
                        .schedulingPeriod("* 0/5 * * * ?")
                        .build()
        );
    }

    @Test
    public void listInstances() {
        DfreeNifi dfreeNifi = createDfreeNifiForTest();

        List<DfreeInstance> instances = dfreeNifi.listInstances("ods");
        assertFalse(instances.isEmpty());
    }
}
