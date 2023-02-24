package org.apache.dfree.jpa.repository.rw.pg;

import org.apache.dfree.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
public interface PgUserRepository extends JpaRepository<User, String> {
}
