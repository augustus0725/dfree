package org.apache.dfree.app.api;


import org.apache.dfree.app.api.vo.i.UserVO;
import org.apache.dfree.commons.standard.RestResponse;
import org.apache.dfree.jpa.entity.User;
import org.apache.dfree.jpa.entity.base.Page;
import org.apache.dfree.jpa.entity.projections.UserFieldsAge;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
public interface Users {
    /**
     * Save a user object.
     *
     * @param user is the object needed to be saved.
     * @return saved User Object.
     */
    RestResponse<User> save(UserVO user);

    /**
     * Find user info by user name.
     *
     * @param name        We find info by name.
     * @param currentPage Current page number.
     * @param pageSize    Page size.
     * @return A page of UserFieldsAge objects.
     */
    RestResponse<Page<UserFieldsAge>> findByName(String name, int currentPage, int pageSize);
}
