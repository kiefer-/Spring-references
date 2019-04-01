package me.shouheng.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author shouh, 2019/3/31-15:59
 */
@Data
public abstract class AbstractPackVo implements Serializable {

    private static final long serialVersionUID = -2119661016457733317L;

    private Boolean success = true;

    private List<ClientMessage> messages;

    private Long udf1;

    private String udf2;

    private String udf3;

    private String udf4;

    private String udf5;

    private String udf6;

}
