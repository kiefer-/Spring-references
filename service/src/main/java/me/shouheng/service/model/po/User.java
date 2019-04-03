package me.shouheng.service.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.shouheng.common.model.AbstractPO;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shouh, 2019/4/3-22:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "gt_user")
public class User extends AbstractPO {

    @Column(name = "email")
    private String email;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
}
