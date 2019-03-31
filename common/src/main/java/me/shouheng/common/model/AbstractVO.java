package me.shouheng.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shouh, 2019/3/31-15:57
 */
public abstract class AbstractVO implements Serializable {

    private static final long serialVersionUID = 1;

    private Long id;

    private Date createdTime;

    private Date updatedTime;

    private String remark;

    private Integer lockVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }
}
