package me.shouheng.common.model;

import java.io.Serializable;

/**
 * @author shouh, 2019/3/31-16:00
 */
public class ClientMessage implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    private String code;

    private String message;

    private String messageCN;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCN() {
        return messageCN;
    }

    public void setMessageCN(String messageCN) {
        this.messageCN = messageCN;
    }
}
