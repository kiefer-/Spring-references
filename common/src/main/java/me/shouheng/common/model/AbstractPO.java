package me.shouheng.common.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shouh, 2019/3/31-14:15
 */
@Data
public abstract class AbstractPO implements Serializable {

    private static final long serialVersionUID = 6982434571375510313L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "remark")
    private String remark;

    @Version
    @Column(name = "lock_version")
    private Integer lockVersion;

}
