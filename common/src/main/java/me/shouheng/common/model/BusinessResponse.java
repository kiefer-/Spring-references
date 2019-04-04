package me.shouheng.common.model;

import lombok.Data;

import java.util.List;

/**
 * 封装的响应对象，要求客户端与服务端响应数据结构统一，作为参考
 * 对应的请求类型参考{@link BusinessRequest}
 *
 * @author shouh, 2019/4/4-23:29
 */
@Data
public class BusinessResponse<T> {

    /**
     * 是否成功（整个批次处理结果，如服务请求发送的是多条数据并允许部分成功，此标记为 True
     * 在这种场景下必须逐条检查数据状态
     */
    private Boolean isSuccess;

    /**
     * 服务端标志，视应用场景与服务端自行约定其含义
     */
    private Long serverFlag;

    /**
     * 返回的错误信息
     */
    private String serverMessage;

    /**
     * 响应数据
     */
    private T responseData;

    /**
     * 响应数据的列表（多条）
     */
    private List<T> responseDataList;

    private Long udf1;
}
