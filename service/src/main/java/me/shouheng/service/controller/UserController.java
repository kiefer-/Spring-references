package me.shouheng.service.controller;

import me.shouheng.common.util.TextUtils;
import me.shouheng.service.common.util.CommonUtils;
import me.shouheng.service.model.vo.PackUserVo;
import me.shouheng.service.model.vo.UserVo;
import me.shouheng.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static me.shouheng.service.controller.UserController.PATH_PREFIX;

/**
 * @author shouh, 2019/4/2-21:53
 */
@Controller
@RequestMapping(path = {PATH_PREFIX})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    static final String PATH_PREFIX = "/user";

    private static final String LOGIN = "/v1/login";

    private static final String REGISTER = "/v1/register";

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(path = REGISTER, method = RequestMethod.POST)
    public PackUserVo register(@RequestBody UserVo user) {
        logger.info("app register info {}", user);
        if (TextUtils.isEmpty(user.getEmail()) || TextUtils.isEmpty(user.getPassword())) {
            return CommonUtils.getFailedPackVo(PackUserVo.class);
        }
        PackUserVo packUserVo = CommonUtils.getSuccessPackVo(PackUserVo.class);
        // ……省略校验的操作，直接插入数据库
        userService.createUser(user);
        logger.info("app register result {}", packUserVo);
        return packUserVo;
    }


    /**
     * 模拟用户登录的接口，该接口的功能比较简单，仅用来测试
     * 如果想要实现更加标准的登录功能，除了返回标准 Token 之外，还要记录登录的日志，
     * 登录日志中还要增加更多的用户相关的信息，包括移动端的设备信息等
     * 登录日志参考{@link me.shouheng.service.model.po.SysLoginLog}
     *
     * @param user 用户信息
     * @return 登录结果
     */
    @ResponseBody
    @RequestMapping(path = LOGIN, method = RequestMethod.POST)
    public PackUserVo login(@RequestBody UserVo user) {
        logger.info("app login info {}", user);
        if (TextUtils.isEmpty(user.getEmail()) || TextUtils.isEmpty(user.getPassword())) {
            return CommonUtils.getFailedPackVo(PackUserVo.class);
        }
        // 1.登录
        PackUserVo packUserVo = userService.login(user);
        // 2.记录登录日志，略
        logger.info("app login result {}", packUserVo);
        return packUserVo;
    }
}
