package org.apache.dfree.app.controller;

import lombok.RequiredArgsConstructor;
import org.apache.dfree.app.api.Hello;
import org.apache.dfree.app.api.vo.i.HelloVoIn;
import org.apache.dfree.app.api.vo.o.HelloVoOut;
import org.apache.dfree.commons.annotation.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * It's an example.
 *
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
// @RestController
@RequiredArgsConstructor(onConstructor__ = {@Autowired})
public class HelloController {
    private final Hello hello;

    @PostMapping({"/hello"})
    @Loggable
    public HelloVoOut hello(@RequestBody HelloVoIn hvi) {
        return hello.say(hvi);
    }
}
