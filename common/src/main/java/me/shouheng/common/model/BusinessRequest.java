package me.shouheng.common.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 封装的请求对象，作为参考，这种封装方式要求客户端的数据结构与服务端统一
 * 对应的响应类型封装，参考{@link BusinessResponse}
 *
 * @author shouh, 2019/4/4-23:22
 */
@Data
public class BusinessRequest<T> {

    /**
     * 客户端版本
     */
    private Integer clientVersion;

    /**
     * 客户端时间
     */
    private Date clientTime;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 本机IMEI
     */
    private String iMEI;

    /**
     * 本机IMSI
     */
    private String iMSI;

    /**
     * 本机设备ID
     */
    private String deviceID;

    /**
     * 用户ID
     */
    private Long userID;

    /**
     * 用户token
     */
    private String token;

    /**
     * 请求类型（需要时加上）
     */
    private String requestType;

    /**
     * 请求数据
     */
    private T requestData;

    /**
     * 列表类型的请求数据
     */
    private List<T> requestDataList;

}
