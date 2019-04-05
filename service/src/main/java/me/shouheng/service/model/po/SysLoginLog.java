package me.shouheng.service.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.shouheng.common.model.AbstractPO;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 设备登录日志的参考类，除了基础的用户信息之外，应该还要增加一些与业务相关的信息
 *
 * @author shouh, 2019/4/5-12:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "gt_sys_login_log")
public class SysLoginLog extends AbstractPO {

    /**
     * 登录用户的 id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 登录用户的邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 登录用户的用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 登录设备 imei
     */
    @Column(name = "imei")
    private String IMEI;

    /**
     * 登录设备 imsi
     */
    @Column(name = "imsi")
    private String IMSI;

    /**
     * 登录设备 IP 地址
     */
    @Column(name = "ip_address")
    private String ipAddress;

    /**
     * 登录设备 ID
     */
    @Column(name = "device_id")
    private String deviceId;

    /**
     * 登录的事件（系统时间）
     */
    @Column(name = "login_time")
    private String loginTime;

    // ...其他用户身份信息
}
