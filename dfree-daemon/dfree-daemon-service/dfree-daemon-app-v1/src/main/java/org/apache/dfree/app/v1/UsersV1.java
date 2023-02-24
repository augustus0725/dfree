package org.apache.dfree.app.v1;

import lombok.RequiredArgsConstructor;
import org.apache.dfree.app.api.Users;
import org.apache.dfree.app.api.vo.i.UserVO;
import org.apache.dfree.commons.standard.RestResponse;
import org.apache.dfree.jpa.entity.User;
import org.apache.dfree.jpa.entity.base.Page;
import org.apache.dfree.jpa.entity.projections.UserFieldsAge;
import org.apache.dfree.jpa.repository.r.UserReadOnlyRepository;
import org.apache.dfree.jpa.repository.rw.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
@Service
@RequiredArgsConstructor(onConstructor__ = {@Autowired})
public class UsersV1 implements Users {
    private final UserRepository userRepository;
    private final UserReadOnlyRepository userReadOnlyRepository;

    @Override
    public RestResponse<User> save(UserVO user) {
        return RestResponse.ok(userRepository.save(user));
    }


    @Override
    public RestResponse<Page<UserFieldsAge>> findByName(String name, int currentPage, int pageSize) {
        org.springframework.data.domain.Page<UserFieldsAge> pageData =
                userReadOnlyRepository.findByName(name, PageRequest.of(currentPage, pageSize));

        return RestResponse.ok(Page.of(pageData, currentPage, pageSize));
    }
}