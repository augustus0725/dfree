package org.apache.dfree.app.api.vo.i;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.dfree.jpa.entity.User;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {
}
