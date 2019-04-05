package me.shouheng.service.common.aop;

import me.shouheng.common.model.AbstractPackVo;
import me.shouheng.common.model.ClientMessage;
import me.shouheng.service.common.exception.OptLockException;
import me.shouheng.service.common.exception.SystemException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shouh, 2019/4/1-20:56
 */
public class ServiceMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMethodInterceptor.class);

    /**
     * 当指定的方法执行的事件大于下面的这个值的时候就将信息记录到日志中，单位：毫秒
     */
    private static final long LOG_METHOD_EXECUTION_TIME = 1000L;

    /**
     * 用来对被拦截的方法进行处理，目前主要的责任有两个：
     * 1.进行数据库连接、提交和回滚；
     * 2.对发生错误的信息进行包装，返回错误结果。
     *
     * @param methodInvocation 调用的方法
     * @return 返回方法结果
     * @throws Throwable 抛出异常
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method targetMethod = methodInvocation.getMethod();
        Object ret;
        try {
            // 初始化数据库连接
//            SqlSessionHolder.initReuseSqlSession();
            // 进行安全校验并触发方法
            ret = checkSecurityAndInvokeBizMethod(methodInvocation);
            // 提交事务
//            SqlSessionHolder.commitSession();
        } catch (Exception e) {
            logger.error("Error calling " + targetMethod.getName() + " : " + e);
            // 事务回滚
//            SqlSessionHolder.rollbackSession();
            // 包装异常信息之后将其返回给客户端
            return this.createExceptionResult(methodInvocation, e);
        } finally {
            // 清空会话信息
//            SqlSessionHolder.clearSession();
        }
        return ret;
    }

    /**
     * 检测传入的方法的信息，执行并返回执行结果
     *
     * @param methodInvocation 触发的方法
     * @return 方法执行的结果
     * @throws Throwable 方法执行过程中抛出的异常
     */
    private Object checkSecurityAndInvokeBizMethod(MethodInvocation methodInvocation) throws Throwable {
        Object rlt = null;
        Throwable failure = null;
        long expend;
        try {
            long start = System.currentTimeMillis();
            rlt = methodInvocation.proceed();
            expend = System.currentTimeMillis() - start;
            if (expend >= LOG_METHOD_EXECUTION_TIME) {
                logger.info("Thread: " + Thread.currentThread().getName()
                        + " method: " + methodInvocation.getMethod().getName()
                        + " time: |" + (expend) + "| ms");
            }
        } catch (IllegalAccessException e) {
            failure = e;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                failure = e.getTargetException();
            } else {
                failure = e;
            }
        }
        handleFailure(failure, methodInvocation.getMethod());
        return rlt;
    }

    /**
     * 处理错误信息
     *
     * @param failure 抛出的异常
     * @param targetMethod 抛出异常的方法
     * @throws Throwable 继续抛出异常
     */
    private void handleFailure(Throwable failure, Method targetMethod) throws Throwable {
        if (failure != null) {
            // 剥掉层层外壳
            while (failure instanceof RemoteException
                    || failure instanceof RemoteAccessException
                    || RuntimeException.class.equals(failure.getClass())) {
                if (failure.getCause() != null) {
                    failure = failure.getCause();
                } else {
                    break;
                }
            }

            // 如果是运行时异常或者错误，就直接抛出
            if (failure instanceof RuntimeException || failure instanceof Error) {
                throw failure;
            }

            // 如果是函数声明的异常
            Class<?>[] excTypes = targetMethod.getExceptionTypes();
            for (Class<?> excType : excTypes) {
                if (excType.isInstance(failure)) {
                    throw failure;
                }
            }
        }
    }

    /**
     * 根据异常信息和调用的方法信息创建包装类将错误信息返回
     *
     * @param invocation 触发的方法
     * @param exception 异常信息
     * @return 错误信息的包装类
     */
    private Object createExceptionResult(MethodInvocation invocation, Exception exception) {
        Class returnType = invocation.getMethod().getReturnType();
        Object returnInstance = null;
        try {
            logger.warn("Thread: " + Thread.currentThread().getName() + " exception type: ", exception);
            returnInstance = returnType.newInstance();
            if (returnInstance instanceof AbstractPackVo) {
                List<ClientMessage> megs = getClientMessage(exception);
                ((AbstractPackVo) returnInstance).setMessages(megs);
                ((AbstractPackVo) returnInstance).setSuccess(false);
            }
        } catch (Exception e) {
            logger.error("Thread: " + Thread.currentThread().getName() + " exception type: ", exception);
        }
        return returnInstance;
    }

    /**
     * 获取包装之后的异常信息
     *
     * @param exception 原始的异常信息
     * @return 包装之后的异常信息
     */
    private List<ClientMessage> getClientMessage(Exception exception) {
        List<ClientMessage> messages = new LinkedList<>();
        if (exception instanceof OptLockException) {
            ClientMessage msg = OptLockException.getErrorMessage(exception);
            messages.add(msg);
        } else {
            ClientMessage msg = SystemException.getErrorMessage(exception);
            messages.add(msg);
        }
        return messages;
    }

}
