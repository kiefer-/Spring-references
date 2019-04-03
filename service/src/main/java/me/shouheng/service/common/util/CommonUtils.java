package me.shouheng.service.common.util;

import me.shouheng.common.model.AbstractPackVo;

/**
 * @author shouh, 2019/4/3-23:11
 */
public class CommonUtils {

    public static <PackVo extends AbstractPackVo> PackVo getSuccessPackVo(Class<PackVo> clazz) {
        try {
            PackVo packVo =  clazz.newInstance();
            packVo.setSuccess(true);
            return packVo;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <PackVo extends AbstractPackVo> PackVo getFailedPackVo(Class<PackVo> clazz) {
        try {
            PackVo packVo =  clazz.newInstance();
            packVo.setSuccess(false);
            return packVo;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
