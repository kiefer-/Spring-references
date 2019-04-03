package me.shouheng.service.model.vo;

import me.shouheng.common.model.AbstractPackVo;

import java.util.List;

public class PackUserVo extends AbstractPackVo {
    private static final long serialVersionUID = 1L;

    private UserVo vo;

    private List<UserVo> voList;

    public UserVo getVo() {
        return vo;
    }

    public void setVo(UserVo vo) {
        this.vo = vo;
    }

    public List<UserVo> getVoList() {
        return voList;
    }

    public void setVoList(List<UserVo> voList) {
        this.voList = voList;
    }

}