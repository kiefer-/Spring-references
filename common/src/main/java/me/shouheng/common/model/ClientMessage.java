package me.shouheng.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shouh, 2019/3/31-16:00
 */
@Data
public class ClientMessage implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    private String code;

    private String message;

    private String messageCN;

}
