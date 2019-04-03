package me.shouheng.service;

import me.shouheng.service.base.MockTestUtil;
import me.shouheng.service.base.SpringBaseTest;
import me.shouheng.service.model.so.UserSo;
import me.shouheng.service.model.vo.UserVo;
import me.shouheng.service.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceTest extends SpringBaseTest {

    @Autowired
    private UserService userService;

    public UserVo create(){
        UserVo vo = MockTestUtil.getJavaBean(UserVo.class);
        return userService.createUser(vo).getVo();
    }

    @Test
    public void testCreate(){
        UserVo vo = this.create();
        Assert.assertTrue(vo!= null);
    }

    @Test
    public void testSearch(){
        UserVo vo = this.create();
        UserSo so = new UserSo();
        List<UserVo> voList = userService.searchUser(so).getVoList();
        Assert.assertTrue(voList != null && voList.size() > 0);
    }

    @Test
    public void testSearchCount(){
        UserVo vo = this.create();
        UserSo so = new UserSo();
        long count = userService.searchUserCount(so).getUdf1();
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testUpdate(){
        UserVo vo = this.create();
        UserVo voTemp = userService.getUser(vo.getId()).getVo();
        voTemp.setRemark("remark was updated");
        UserVo updateRes = userService.updateUser(voTemp).getVo();
        Assert.assertTrue(updateRes !=null && "remark was updated".equals(updateRes.getRemark()));
    }

}
