package me.shouheng.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shouh, 2019/3/31-15:57
 */
@Data
public abstract class AbstractVO implements Serializable {

    private static final long serialVersionUID = 1;

    private Long id;

    private Date createdTime;

    private Date updatedTime;

    private String remark;

    private Integer lockVersion;

}
