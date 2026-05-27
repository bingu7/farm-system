package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.Utils.AccountSanitizer;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.Utils.PasswordUtils;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员业务处理
 **/
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 新增
     */
    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (ObjectUtil.isNotNull(dbAdmin)) {
            throw new CustomException("用户已存在");
        }
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword("admin");
        }
        admin.setPassword(PasswordUtils.encode(admin.getPassword()));
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Admin admin) {
        Admin dbAdmin = adminMapper.selectById(admin.getId());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (!dbAdmin.getUsername().equals(admin.getUsername())) {
            Admin adminByUsername = adminMapper.selectByUsername(admin.getUsername());
            if (ObjectUtil.isNotNull(adminByUsername) && !adminByUsername.getId().equals(admin.getId())) {
                throw new CustomException("用户已存在");
            }
        }
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword(dbAdmin.getPassword());
        } else {
            admin.setPassword(PasswordUtils.encode(admin.getPassword()));
        }
        admin.setRole("ADMIN");
        adminMapper.updateById(admin);
    }

    /**
     * 根据ID查询
     */
    public Admin selectById(Integer id) {
        return AccountSanitizer.sanitize(adminMapper.selectById(id));
    }

    /**
     * 查询所有
     */
    public List<Admin> selectAll(Admin admin) {
        return AccountSanitizer.sanitizeList(adminMapper.selectAll(admin));
    }

    /**
     * 分页查询
     */
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        AccountSanitizer.sanitizeList(list);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (!PasswordUtils.matches(account.getPassword(), dbAdmin.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        if (PasswordUtils.needsUpgrade(dbAdmin.getPassword())) {
            dbAdmin.setPassword(PasswordUtils.encode(account.getPassword()));
            adminMapper.updateById(dbAdmin);
        }
        AccountSanitizer.sanitize(dbAdmin);
        return dbAdmin;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (!PasswordUtils.matches(account.getPassword(), dbAdmin.getPassword())) {
            throw new CustomException("原密码错误");
        }
        if (ObjectUtil.isEmpty(account.getNewPassword())) {
            throw new CustomException("新密码不能为空");
        }
        dbAdmin.setPassword(PasswordUtils.encode(account.getNewPassword()));
        adminMapper.updateById(dbAdmin);
    }

}
