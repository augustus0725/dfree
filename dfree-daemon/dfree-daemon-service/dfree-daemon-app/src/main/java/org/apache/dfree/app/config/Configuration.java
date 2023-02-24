package org.apache.dfree.app.config;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.dfree.nifi.api.DfreeNifi;
import org.apache.dfree.nifi.api.exception.DfreeNifiException;
import org.apache.dfree.nifi.v1p77.NifiV1p17;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@org.springframework.context.annotation.Configuration
@Slf4j
public class Configuration {
    @Bean
    public DfreeNifi dfreeNifi(@Autowired Environment env) {
        String nifiHost = env.getProperty("nifi.host");
        if (Strings.isNullOrEmpty(nifiHost)) {
            log.error("Please config nifi.host!");
            throw new DfreeNifiException("Please config nifi.host!");
        }
        return new NifiV1p17(nifiHost);
    }
}
