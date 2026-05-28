package com.example.controller;

import com.example.common.Result;
import com.example.Utils.AuthUtils;
import com.example.entity.Account;
import com.example.entity.Orders;
import com.example.service.OrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端操作接口
 **/
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Orders orders) {
        Account currentUser = AuthUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());
        ordersService.add(orders);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        AuthUtils.requireAdmin();
        ordersService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Orders orders) {
        Orders dbOrders = ordersService.selectById(orders.getId());
        if (dbOrders == null) {
            return Result.error("订单不存在");
        }
        Account currentUser = AuthUtils.getCurrentUser();
        AuthUtils.requireSelfOrAdmin(dbOrders.getUserId());
        ordersService.updateStatus(orders.getId(), orders.getStatus(), currentUser);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Orders orders = ordersService.selectById(id);
        if (orders == null) {
            return Result.success();
        }
        AuthUtils.requireSelfOrAdmin(orders.getUserId());
        return Result.success(orders);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Orders orders) {
        Account currentUser = AuthUtils.getCurrentUser();
        if (!AuthUtils.isAdmin(currentUser)) {
            orders.setUserId(currentUser.getId());
        }
        List<Orders> list = ordersService.selectAll(orders);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Orders orders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        Account currentUser = AuthUtils.getCurrentUser();
        if (!AuthUtils.isAdmin(currentUser)) {
            orders.setUserId(currentUser.getId());
        }
        PageInfo<Orders> page = ordersService.selectPage(orders, pageNum, pageSize);
        return Result.success(page);
    }

}
