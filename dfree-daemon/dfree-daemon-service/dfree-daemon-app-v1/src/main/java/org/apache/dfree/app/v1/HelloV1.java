package org.apache.dfree.app.v1;

import org.apache.dfree.app.api.Hello;
import org.apache.dfree.app.api.vo.i.HelloVoIn;
import org.apache.dfree.app.api.vo.o.HelloVoOut;
import org.springframework.stereotype.Service;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
@Service
public class HelloV1 implements Hello {
    @Override
    public HelloVoOut say(HelloVoIn c) {
        return HelloVoOut.builder().content(c.getContent()).build();
    }
}
