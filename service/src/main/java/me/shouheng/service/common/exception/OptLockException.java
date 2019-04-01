package me.shouheng.service.common.exception;

import me.shouheng.common.exception.BizException;
import me.shouheng.common.model.ClientMessage;
import me.shouheng.service.common.util.ErrorDispUtils;

/**
 * @author shouh, 2019/3/31-15:48
 */
public class OptLockException extends BizException {

    private static final long serialVersionUID = -4487429038874269500L;

    private static final String ERR_OPT_LOCK_CODE = "E000000000000001";

    public OptLockException() {
    }

    public OptLockException(String message) {
        super(message);
    }

    public OptLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptLockException(Throwable cause) {
        super(cause);
    }

    public OptLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static ClientMessage getErrorMessage(Exception exception) {
        ClientMessage msg = new ClientMessage();
        msg.setCode(ERR_OPT_LOCK_CODE);
        msg.setMessage(exception.getMessage());
        msg.setMessageCN(ErrorDispUtils.getInstance().getValue(ERR_OPT_LOCK_CODE));
        return msg;
    }
}
