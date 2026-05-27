package com.example.service;

import com.example.entity.StatVo;
import com.example.mapper.GoodsMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StatisticsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private UserMapper userMapper;

    public Map<String, Object> getStatistics(Integer userId, String role) {
        Map<String, Object> map = new HashMap<>();

        if ("ADMIN".equals(role)) {
            // 管理员统计
            map.put("card1", userMapper.selectAll(null).size());
            map.put("card2", goodsMapper.selectAll(null).size());
            map.put("card3", ordersMapper.selectAll(null).size());
            map.put("card1Text", "全站用户数");
            map.put("card2Text", "全站商品数");
            map.put("card3Text", "全站总订单量");

            map.put("pieData", goodsMapper.countByCategory());
            map.put("barData", ordersMapper.countAllStatus());
        } else {
            // 用户个人统计
            // 1. 我的订单总数
//            long totalOrders = ordersMapper.countUserStatus(userId).stream()
//                    .mapToLong(m -> (Long)m.getValue()).sum();
            List<StatVo> userStatusList = ordersMapper.countUserStatus(userId);
            long totalOrders = userStatusList.stream().mapToLong(StatVo::getValue).sum();
            map.put("card1", totalOrders);
            map.put("card1Text", "我的订单总数");

            // 2. 我的累计消费额 (新推荐)
            // 注意：你需要在 Service 中注入 ordersMapper
            Double totalSpent = ordersMapper.totalSpentByUser(userId);
            map.put("card2", "￥" + String.format("%.2f", totalSpent));
            map.put("card2Text", "累计消费金额");

            // 3. 待处理订单数 (新推荐)
            Long pendingCount = ordersMapper.countPendingOrders(userId);
            map.put("card3", pendingCount);
            map.put("card3Text", "待收货订单数");

            // 饼图和柱状图
            map.put("pieData", ordersMapper.countUserCategory(userId));
            map.put("barData", ordersMapper.countUserStatus(userId));
        }
        return map;
    }
}
