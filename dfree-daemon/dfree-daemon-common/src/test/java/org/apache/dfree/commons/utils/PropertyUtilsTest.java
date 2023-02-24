package org.apache.dfree.commons.utils;


import org.apache.dfree.commons.standard.RestResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 陈虎
 * @date 2022-06-15 13:45
 */
public class PropertyUtilsTest {

    @Test
    public void copyNotNullProperty() {
        RestResponse<String> origin = RestResponse.ok("pass");
        origin.setMessage(null);
        RestResponse<String> later = RestResponse.error("error", "has error");
        PropertyUtils.copyNotNullProperty(origin, later);
        Assert.assertEquals("has error", later.getMessage());
        Assert.assertEquals(200, later.getCode());
    }
}
