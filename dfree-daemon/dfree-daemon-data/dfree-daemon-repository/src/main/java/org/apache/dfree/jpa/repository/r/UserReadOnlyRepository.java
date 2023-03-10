package org.apache.dfree.jpa.repository.r;

import org.apache.dfree.jpa.entity.User;
import org.apache.dfree.jpa.entity.projections.UserFieldsAge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
public interface UserReadOnlyRepository extends JpaRepository<User, String> {
    /**
     * Query user by user name.
     *
     * @param name    is query parameter.
     * @param request is page request parameter.
     * @return A page of UserFieldsAge Objects.
     */
    Page<UserFieldsAge> findByName(String name, PageRequest request);
}
