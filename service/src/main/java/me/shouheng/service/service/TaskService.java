package me.shouheng.service.service;

import me.shouheng.service.model.so.TaskSo;
import me.shouheng.service.model.vo.PackTaskVo;
import me.shouheng.service.model.vo.TaskVo;

/**
 * @author shouh
 */
public interface TaskService {

    PackTaskVo createTask(TaskVo vo);

    PackTaskVo getTask(Long primaryKey);

    PackTaskVo updateTask(TaskVo vo);

    PackTaskVo deleteTask(Long primaryKey);

    PackTaskVo searchTask(TaskSo so);

    PackTaskVo searchTaskCount(TaskSo so);

}
