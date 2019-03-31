package me.shouheng.service;

import me.shouheng.service.base.MockTestUtil;
import me.shouheng.service.base.SpringBaseTest;
import me.shouheng.service.model.so.TaskSo;
import me.shouheng.service.model.vo.TaskVo;
import me.shouheng.service.service.TaskService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TaskServiceTest extends SpringBaseTest {

    @Autowired
    private TaskService taskService;

    public TaskVo create(){
        TaskVo vo = MockTestUtil.getJavaBean(TaskVo.class);
        return taskService.createTask(vo).getVo();
    }

    @Test
    public void testCreate(){
        TaskVo vo = this.create();
        Assert.assertTrue(vo!= null);
    }

    @Test
    public void testSearch(){
        TaskVo vo = this.create();
        TaskSo so = new TaskSo();
        List<TaskVo> voList = taskService.searchTask(so).getVoList();
        Assert.assertTrue(voList != null && voList.size() > 0);
    }

    @Test
    public void testSearchCount(){
        TaskVo vo = this.create();
        TaskSo so = new TaskSo();
        long count = taskService.searchTaskCount(so).getUdf1();
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testUpdate(){
        TaskVo vo = this.create();
        TaskVo voTemp = taskService.getTask(vo.getId()).getVo();
        voTemp.setRemark("remark was updated");
        TaskVo updateRes = taskService.updateTask(voTemp).getVo();
        Assert.assertTrue(updateRes !=null && "remark was updated".equals(updateRes.getRemark()));
    }

}
