package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.Utils.AuthUtils;
import com.example.entity.Account;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.exception.CustomException;
import com.example.mapper.OrdersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrdersService {

    private static final String STATUS_CANCELLED = "已取消";
    private static final String STATUS_PENDING_PAY = "待支付";
    private static final String STATUS_PENDING_SHIP = "待发货";
    private static final String STATUS_PENDING_RECEIVE = "待收货";
    private static final String STATUS_COMPLETED = "已完成";

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private GoodsService goodsService;

    @Transactional
    public void add(Orders orders) {
        if (orders.getGoodsId() == null) {
            throw new CustomException("请选择商品");
        }
        if (orders.getNum() == null || orders.getNum() <= 0) {
            throw new CustomException("购买数量必须大于0");
        }

        Goods goods = goodsService.selectById(orders.getGoodsId());
        if (goods == null) {
            throw new CustomException("商品不存在");
        }

        if (!goodsService.decreaseStock(orders.getGoodsId(), orders.getNum())) {
            throw new CustomException("商品库存不足");
        }

        orders.setOrderNo(IdUtil.fastSimpleUUID());
        orders.setTime(DateUtil.now());
        orders.setStatus(STATUS_PENDING_PAY);
        ordersMapper.insert(orders);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    @Transactional
    public void updateStatus(Integer orderId, String targetStatus, Account currentUser) {
        if (orderId == null) {
            throw new CustomException("订单不存在");
        }
        Orders dbOrders = ordersMapper.selectById(orderId);
        if (dbOrders == null) {
            throw new CustomException("订单不存在");
        }
        if (targetStatus == null || targetStatus.isBlank()) {
            throw new CustomException("订单状态不能为空");
        }
        if (targetStatus.equals(dbOrders.getStatus())) {
            return;
        }

        assertAllowedStatusChange(dbOrders.getStatus(), targetStatus, currentUser);

        if (STATUS_CANCELLED.equals(targetStatus)) {
            goodsService.increaseStock(dbOrders.getGoodsId(), dbOrders.getNum());
        }

        dbOrders.setStatus(targetStatus);
        ordersMapper.updateById(dbOrders);
    }

    private void assertAllowedStatusChange(String currentStatus, String targetStatus, Account currentUser) {
        boolean allowed;
        if (AuthUtils.isAdmin(currentUser)) {
            allowed = STATUS_PENDING_SHIP.equals(currentStatus) && STATUS_PENDING_RECEIVE.equals(targetStatus);
        } else {
            allowed = (STATUS_PENDING_PAY.equals(currentStatus) && STATUS_PENDING_SHIP.equals(targetStatus))
                    || (STATUS_PENDING_PAY.equals(currentStatus) && STATUS_CANCELLED.equals(targetStatus))
                    || (STATUS_PENDING_RECEIVE.equals(currentStatus) && STATUS_COMPLETED.equals(targetStatus));
        }

        if (!allowed) {
            throw new CustomException("订单状态流转不合法");
        }
    }

    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }
}
