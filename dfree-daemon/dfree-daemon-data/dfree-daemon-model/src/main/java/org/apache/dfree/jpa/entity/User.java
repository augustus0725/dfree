package org.apache.dfree.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.dfree.jpa.entity.base.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
@Entity
@Table(name = "t_user")
@Data
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User extends BaseEntity {
    @Column
    private String name;
    @Column
    private int age;
}
