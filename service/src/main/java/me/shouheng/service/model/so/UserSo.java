package me.shouheng.service.model.so;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.shouheng.common.model.query.SearchObject;
import org.springframework.stereotype.Repository;

@Data
@Repository
@EqualsAndHashCode(callSuper = true)
public class UserSo extends SearchObject {
    private static final long serialVersionUID = 1L;

    private String email;

    private String userName;

    private String password;
}
