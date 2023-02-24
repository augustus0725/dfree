package org.apache.dfree.jpa.entity.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    private String id;

    @Column
    @CreatedDate
    private Timestamp createdDate;
    @LastModifiedDate
    @Column
    private Long lastModifiedDate;
}
