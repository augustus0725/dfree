package org.apache.dfree.nifi.api;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public abstract class AbstractDfreeNifi implements DfreeNifi {
    // example http://192.168.0.16:8022
    protected final String nifiConnectString;
    protected final RestTemplate restTemplate = new RestTemplate();

    protected AbstractDfreeNifi(String nifiConnectString) {
        this.nifiConnectString = nifiConnectString;
        customRestTemplate();
    }

    private void customRestTemplate() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.setMessageConverters(Collections.singletonList(converter));
    }
}
