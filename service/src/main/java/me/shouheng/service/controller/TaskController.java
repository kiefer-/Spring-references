package me.shouheng.service.controller;

import me.shouheng.service.model.so.TaskSo;
import me.shouheng.service.model.vo.PackTaskVo;
import me.shouheng.service.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static me.shouheng.service.controller.TaskController.PATH_PREFIX;

/**
 * 用于测试的控制器
 *
 * @author shouh, 2019/3/30-20:57
 */
@Controller
@RequestMapping(path = {PATH_PREFIX})
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    static final String PATH_PREFIX = "/task";

    private static final String LIST = "/v1/all";

    private static final String LIST2 = "/v2/all";

    private static final String PAGE = "/page";

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 测试用来发送 restful 类型的请求的接口
     *
     * @param taskSo 请求对象，Json 类型，放置在 body 中
     * @return 返回对象
     */
    @ResponseBody
    @RequestMapping(value = LIST, method = RequestMethod.POST)
    public PackTaskVo listAll(@RequestBody TaskSo taskSo) {
        logger.info("----------- received : " + taskSo);
        PackTaskVo packTaskVo = taskService.searchTask(taskSo);
        logger.info("----------- result : " + packTaskVo);
        return packTaskVo;
    }

    /**
     * {@link #listAll(TaskSo)} 的 v2 版本
     *
     * @param taskSo so 对象
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = LIST2, method = RequestMethod.POST)
    public PackTaskVo listAll2(@RequestBody TaskSo taskSo) {
        logger.info("----------- received : " + taskSo);
        PackTaskVo packTaskVo = taskService.searchTask(taskSo);
        logger.info("----------- result : " + packTaskVo);
        return packTaskVo;
    }

    /**
     * 测试用来请求 jsp 页面的接口
     *
     * @return jsp 页面（名称），映射到 views/task.jsp
     */
    @RequestMapping(value = PAGE, method = RequestMethod.GET)
    public String page() {
        logger.info("----------- requesting test page.");
        return "task";
    }

}
