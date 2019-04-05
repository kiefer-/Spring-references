package me.shouheng.service.service.impl;

import me.shouheng.common.util.DozerBeanUtil;
import me.shouheng.service.dao.UserDAO;
import me.shouheng.service.model.po.User;
import me.shouheng.service.model.so.UserSo;
import me.shouheng.service.model.vo.PackUserVo;
import me.shouheng.service.model.vo.UserVo;
import me.shouheng.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDAO userDAO;

    private DozerBeanUtil dozerBeanUtil;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, DozerBeanUtil dozerBeanUtil) {
        this.userDAO = userDAO;
        this.dozerBeanUtil = dozerBeanUtil;
    }

    @Override
    public PackUserVo createUser(UserVo vo){
        PackUserVo packVo = new PackUserVo();
        User entity = dozerBeanUtil.convert(vo, User.class);
        userDAO.insert(entity);
        packVo.setVo(dozerBeanUtil.convert(entity, UserVo.class));
        return packVo;
    }

    @Override
    public PackUserVo getUser(Long primaryKey){
        PackUserVo packVo = new PackUserVo();
        User returnEntity = userDAO.selectByPrimaryKey(primaryKey);
        packVo.setVo(dozerBeanUtil.convert(returnEntity, UserVo.class));
        return packVo;
    }

    @Override
    public PackUserVo updateUser(UserVo vo){
        PackUserVo packVo = new PackUserVo();
        User po = dozerBeanUtil.convert(vo, User.class);
        userDAO.update(po);
        packVo.setVo(dozerBeanUtil.convert(po, UserVo.class));
        return packVo;
    }

    @Override
    public PackUserVo deleteUser(Long primaryKey){
        PackUserVo packVo = new PackUserVo();
        userDAO.deleteByPrimaryKey(primaryKey);
        return packVo;
    }

    @Override
    public PackUserVo searchUser(UserSo so){
        PackUserVo packVo = new PackUserVo();
        List<UserVo> voList = userDAO.searchVosBySo(so);
        packVo.setVoList(voList);
        return packVo;
    }

    @Override
    public PackUserVo searchUserCount(UserSo so){
        PackUserVo packVo = new PackUserVo();
        Long count = userDAO.searchCountBySo(so);
        packVo.setUdf1(count);
        return packVo;
    }

    @Override
    public PackUserVo login(UserVo user) {
        UserSo userSo = new UserSo();
        userSo.setEmail(user.getEmail());
        PackUserVo packUserVo = searchUser(userSo);
        // ……用户存在，检验是否存在匹配的密码，因为我们不校验重复，所以同一 email 用户可能存在多个
        long cnt = packUserVo.getVoList().stream().filter(u -> u.getPassword().equals(user.getPassword())).count();
        packUserVo.setSuccess(cnt > 0);
        packUserVo.getVoList().forEach(u -> u.setToken("----token"));
        return packUserVo;
    }

}
