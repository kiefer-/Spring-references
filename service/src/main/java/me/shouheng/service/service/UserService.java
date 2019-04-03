package me.shouheng.service.service;

import me.shouheng.service.model.so.UserSo;
import me.shouheng.service.model.vo.PackUserVo;
import me.shouheng.service.model.vo.UserVo;

public interface UserService {

    PackUserVo createUser(UserVo vo);

    PackUserVo getUser(Long primaryKey);

    PackUserVo updateUser(UserVo vo);

    PackUserVo deleteUser(Long primaryKey);

    PackUserVo searchUser(UserSo so);

    PackUserVo searchUserCount(UserSo so);

    PackUserVo login(UserVo userVo);

}
