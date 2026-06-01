package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.Utils.AccountSanitizer;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.Utils.PasswordUtils;
import com.example.Utils.ValidationUtils;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户的业务处理
 **/
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(User user) {
        validateUser(user, ObjectUtil.isNotEmpty(user.getPassword()));
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException("用户已存在");
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword("123");
        }
        user.setPassword(PasswordUtils.encode(user.getPassword()));
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole("USER");
        userMapper.insert(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(User user) {
        validateUser(user, ObjectUtil.isNotEmpty(user.getPassword()));
        User dbUser = userMapper.selectById(user.getId());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException("用户不存在");
        }
        if (!dbUser.getUsername().equals(user.getUsername())) {
            User userByUsername = userMapper.selectByUsername(user.getUsername());
            if (ObjectUtil.isNotNull(userByUsername) && !userByUsername.getId().equals(user.getId())) {
                throw new CustomException("用户已存在");
            }
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(dbUser.getPassword());
        } else {
            ValidationUtils.validatePassword(user.getPassword());
            user.setPassword(PasswordUtils.encode(user.getPassword()));
        }
        user.setRole("USER");
        userMapper.updateById(user);
    }

    /**
     * 根据ID查询
     */
    public User selectById(Integer id) {
        return AccountSanitizer.sanitize(userMapper.selectById(id));
    }

    /**
     * 查询所有
     */
    public List<User> selectAll(User user) {
        return AccountSanitizer.sanitizeList(userMapper.selectAll(user));
    }

    /**
     * 分页查询
     */
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        AccountSanitizer.sanitizeList(list);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        ValidationUtils.requireText(account.getUsername(), "用户名不能为空");
        ValidationUtils.requireText(account.getPassword(), "密码不能为空");
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException("用户不存在");
        }
        if (!PasswordUtils.matches(account.getPassword(), dbUser.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        if (PasswordUtils.needsUpgrade(dbUser.getPassword())) {
            dbUser.setPassword(PasswordUtils.encode(account.getPassword()));
            userMapper.updateById(dbUser);
        }
        AccountSanitizer.sanitize(dbUser);
        return dbUser;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        ValidationUtils.requireText(account.getPassword(), "原密码不能为空");
        ValidationUtils.validatePassword(account.getNewPassword());
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException("用户不存在");
        }
        if (!PasswordUtils.matches(account.getPassword(), dbUser.getPassword())) {
            throw new CustomException("原密码错误");
        }
        if (ObjectUtil.isEmpty(account.getNewPassword())) {
            throw new CustomException("新密码不能为空");
        }
        dbUser.setPassword(PasswordUtils.encode(account.getNewPassword()));
        userMapper.updateById(dbUser);
    }

    /**
     * 注册
     */
    public void register(User user) {
        this.add(user);
    }

    private void validateUser(User user, boolean validatePassword) {
        ValidationUtils.validateUsername(user.getUsername());
        if (validatePassword) {
            ValidationUtils.validatePassword(user.getPassword());
        }
        ValidationUtils.validatePhone(user.getPhone());
        ValidationUtils.validateEmail(user.getEmail());
    }
}
