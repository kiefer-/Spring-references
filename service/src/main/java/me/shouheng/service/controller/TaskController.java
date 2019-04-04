package me.shouheng.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.shouheng.common.model.BusinessRequest;
import me.shouheng.common.model.BusinessResponse;
import me.shouheng.service.model.so.TaskSo;
import me.shouheng.service.model.vo.PackTaskVo;
import me.shouheng.service.model.vo.TaskVo;
import me.shouheng.service.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static me.shouheng.service.controller.TaskController.PATH_PREFIX;

/**
 * 用于测试的控制器
 *
 * @author shouh, 2019/3/30-20:57
 */
@Api(tags = "用于测试的接口")
@Controller
@RequestMapping(path = {PATH_PREFIX})
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    static final String PATH_PREFIX = "/task";

    private static final String LIST1 = "/v1/all";

    private static final String LIST2 = "/v2/all";

    private static final String LIST3 = "/v3/all";

    private static final String PAGE = "/page";

    private static final String UPLOAD = "/upload";

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
    @RequestMapping(value = LIST1, method = RequestMethod.POST)
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
     * 基于 {@link BusinessRequest} 和 {@link BusinessResponse} 的请求和响应类型的接口
     *
     * @param businessRequest 请求对象
     * @return 响应对象
     */
    @ResponseBody
    @RequestMapping(value = LIST3, method = RequestMethod.POST)
    public BusinessResponse<TaskVo> listAll3(@RequestBody BusinessRequest<TaskSo> businessRequest) {
        logger.info("request data : {}", businessRequest);
        TaskSo taskSo = businessRequest.getRequestData();
        PackTaskVo packTaskVo = taskService.searchTask(taskSo);
        // 将查询到的结果封装成 BusinessResponse 并返回
        BusinessResponse<TaskVo> businessResponse = new BusinessResponse<>();
        businessResponse.setIsSuccess(true);
        businessResponse.setResponseData(packTaskVo.getVo());
        if (packTaskVo.getMessages() != null) {
            StringBuilder sb = new StringBuilder();
            packTaskVo.getMessages().forEach(clientMessage -> sb.append(clientMessage.getMessage()));
            businessResponse.setServerMessage(sb.toString());
        }
        logger.info("response data : {}", businessResponse);
        return businessResponse;
    }

    /**
     * 测试用来请求 jsp 页面的接口
     *
     * @return jsp 页面（名称），映射到 views/task.jsp
     */
    @ApiOperation(value = "展示一个 jsp 页面")
    @RequestMapping(value = PAGE, method = RequestMethod.GET)
    public String page() {
        logger.info("----------- requesting test page.");
        return "task";
    }

    /**
     * 用来处理文件上传请求的控制器
     *
     * @param file 要上传的文件
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = UPLOAD, method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // 文件存放服务端的位置
                String rootPath = "d:/tmp";
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                // 写文件到服务器
                File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                file.transferTo(serverFile);
                return "Uploaded file=" +  file.getOriginalFilename();
            } catch (Exception e) {
                return "Failed to upload " +  file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "Failed to upload " +  file.getOriginalFilename() + ", file was empty.";
        }
    }

}
