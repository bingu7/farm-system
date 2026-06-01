package com.example.controller;

import com.example.Utils.AuthUtils;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Orders;
import com.example.exception.CustomException;
import com.example.service.OrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/add")
    public Result add(@RequestBody Orders orders) {
        Account currentUser = AuthUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());
        ordersService.add(orders);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        AuthUtils.requireAdmin();
        ordersService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Orders orders) {
        Orders dbOrders = ordersService.selectById(orders.getId());
        if (dbOrders == null) {
            throw new CustomException("订单不存在");
        }
        Account currentUser = AuthUtils.getCurrentUser();
        AuthUtils.requireSelfOrAdmin(dbOrders.getUserId());
        ordersService.updateStatus(orders.getId(), orders.getStatus(), currentUser);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Orders orders = ordersService.selectById(id);
        if (orders == null) {
            return Result.success();
        }
        AuthUtils.requireSelfOrAdmin(orders.getUserId());
        return Result.success(orders);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Orders orders) {
        Account currentUser = AuthUtils.getCurrentUser();
        if (!AuthUtils.isAdmin(currentUser)) {
            orders.setUserId(currentUser.getId());
        }
        List<Orders> list = ordersService.selectAll(orders);
        return Result.success(list);
    }

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
