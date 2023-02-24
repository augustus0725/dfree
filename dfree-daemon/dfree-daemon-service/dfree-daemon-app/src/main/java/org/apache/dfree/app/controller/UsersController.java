package org.apache.dfree.app.controller;

import org.apache.dfree.app.api.Users;
import org.apache.dfree.app.api.vo.i.UserVO;
import org.apache.dfree.commons.annotation.Loggable;
import org.apache.dfree.commons.standard.RestResponse;
import org.apache.dfree.jpa.entity.User;
import org.apache.dfree.jpa.entity.base.Page;
import org.apache.dfree.jpa.entity.projections.UserFieldsAge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * It's an example.
 *
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
// @RestController
@RequiredArgsConstructor(onConstructor__ = {@Autowired})
public class UsersController {
    private final Users users;


    @PostMapping({"/users"})
    @Loggable
    @Operation(description = "示例用的, 描述了一个保存用户的api, 这里描述要详细一点", summary = "示例用的, 描述了一个保存用户的api")
    public RestResponse<User> hello(@RequestBody UserVO user) {
        return users.save(user);
    }

    @GetMapping({"/users"})
    @Loggable
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "描述返回值",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestResponse.class))})
    })
    public RestResponse<Page<UserFieldsAge>> hello(@Parameter(description = "描述参数name") @RequestParam String name,
                                                   @RequestParam int currentPage, @RequestParam int pageSize) {
        return users.findByName(name, currentPage, pageSize);
    }

}
