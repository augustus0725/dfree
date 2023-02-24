package org.apache.dfree.app.api;

import org.apache.dfree.app.api.vo.i.HelloVoIn;
import org.apache.dfree.app.api.vo.o.HelloVoOut;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
public interface Hello {
    /**
     * It's test method.
     *
     * @param c is input params.
     * @return HelloVoOut Object.
     */
    HelloVoOut say(HelloVoIn c);
}
