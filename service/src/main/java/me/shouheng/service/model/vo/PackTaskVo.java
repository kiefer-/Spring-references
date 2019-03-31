package me.shouheng.service.model.vo;

import me.shouheng.common.model.AbstractPackVo;

import java.util.List;

public class PackTaskVo extends AbstractPackVo {

    private static final long serialVersionUID = 1L;

    private TaskVo vo;

    private List<TaskVo> voList;

    public TaskVo getVo() {
        return vo;
    }

    public void setVo(TaskVo vo) {
        this.vo = vo;
    }

    public List<TaskVo> getVoList() {
        return voList;
    }

    public void setVoList(List<TaskVo> voList) {
        this.voList = voList;
    }

}