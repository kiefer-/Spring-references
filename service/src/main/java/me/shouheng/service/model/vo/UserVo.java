package me.shouheng.service.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.shouheng.common.model.AbstractVO;

/**
 * @author shouh, 2019/4/2-21:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserVo extends AbstractVO {

    private String email;

    private String userName;

    private String password;

    /**
     * 登录成功之后返回的 Token
     */
    private String token;
}
