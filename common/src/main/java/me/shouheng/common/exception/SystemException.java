package me.shouheng.common.exception;

import me.shouheng.common.model.ClientMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shouh, 2019/4/1-21:50
 */
public class SystemException extends BizException {

    private static final long serialVersionUID = -1L;

    private static final Logger logger = LoggerFactory.getLogger(SystemException.class);

    public static final String ERR_NULL_POINTER_CODE = "E000000000000002";

    public static final String ERR_SYS_ERROR_CODE = "E000000000000003";

    public static final String ERR_DAO_ERROR_CODE = "E000000000000004";

    public static ClientMessage getErrorMessage(Exception exception) {
        ClientMessage msg = new ClientMessage();
        if (exception instanceof DAOException) {
            logger.error("DAO Exception");
            msg.setCode(ERR_DAO_ERROR_CODE);
        } else if (exception instanceof NullPointerException) {
            logger.error("NPE Exception");
            msg.setCode(ERR_NULL_POINTER_CODE);
        } else {
            logger.error("Common Exception");
            msg.setCode(ERR_SYS_ERROR_CODE);
        }
        msg.setMessage(exception.getMessage());
        return msg;
    }
}
