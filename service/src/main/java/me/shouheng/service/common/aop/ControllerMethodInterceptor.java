package me.shouheng.service.common.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对 Controller 的异常统一处理，暂时没有写逻辑，可以根据方法的返回的包装类型来返回一个
 * 包含错误信息的包装对象
 *
 * @author shouh, 2019/4/5-14:14
 */
public class ControllerMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return methodInvocation.proceed();
    }

}
