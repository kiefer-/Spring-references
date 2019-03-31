package me.shouheng.service.service.impl;

import me.shouheng.common.util.DozerBeanUtil;
import me.shouheng.service.dao.TaskDAO;
import me.shouheng.service.model.po.Task;
import me.shouheng.service.model.so.TaskSo;
import me.shouheng.service.model.vo.PackTaskVo;
import me.shouheng.service.model.vo.TaskVo;
import me.shouheng.service.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

    private Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private TaskDAO taskDAO;

    private DozerBeanUtil dozerBeanUtil;

    @Autowired
    public TaskServiceImpl(TaskDAO taskDAO, DozerBeanUtil dozerBeanUtil) {
        this.taskDAO = taskDAO;
        this.dozerBeanUtil = dozerBeanUtil;
    }

    @Override
    public PackTaskVo createTask(TaskVo vo){
        PackTaskVo packVo = new PackTaskVo();
        Task entity = dozerBeanUtil.convert(vo, Task.class);
        Task returnEntity = taskDAO.createPOReturnObj(entity);
        packVo.setVo(dozerBeanUtil.convert(returnEntity, TaskVo.class));
        return packVo;
    }

    @Override
    public PackTaskVo getTask(Long primaryKey){
        PackTaskVo packVo = new PackTaskVo();
        Task returnEntity = taskDAO.getPO(primaryKey);
        packVo.setVo(dozerBeanUtil.convert(returnEntity, TaskVo.class));
        return packVo;
    }

    @Override
    public PackTaskVo updateTask(TaskVo vo){
        PackTaskVo packVo = new PackTaskVo();
        Task po = dozerBeanUtil.convert(vo, Task.class);
        Task returnEntity = taskDAO.updatePOReturnObj(po);
        packVo.setVo(dozerBeanUtil.convert(returnEntity, TaskVo.class));
        return packVo;
    }

    @Override
    public PackTaskVo deleteTask(Long primaryKey){
        PackTaskVo packVo = new PackTaskVo();
        taskDAO.deletePOById(primaryKey);
        return packVo;
    }

    @Override
    public PackTaskVo searchTask(TaskSo so){
        PackTaskVo packVo = new PackTaskVo();
        List<TaskVo> voList = taskDAO.searchVOs(so);
        packVo.setVoList(voList);
        return packVo;
    }

    @Override
    public PackTaskVo searchTaskCount(TaskSo so){
        PackTaskVo packVo = new PackTaskVo();
        Long count = taskDAO.searchPOsCount(so);
        packVo.setUdf1(count);
        return packVo;
    }

}
