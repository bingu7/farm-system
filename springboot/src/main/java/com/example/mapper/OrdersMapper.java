package com.example.mapper;

import com.example.entity.Orders;
import com.example.entity.StatVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作orders相关数据接口
*/
public interface OrdersMapper {

    /**
      * 新增
    */
    int insert(Orders orders);

    /**
      * 删除
    */
    @Delete("delete from orders where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Orders orders);

    /**
      * 根据ID查询
    */
    @Select("select * from orders where id = #{id}")
    Orders selectById(Integer id);

    /**
      * 查询所有
    */
    List<Orders> selectAll(Orders orders);

    // 在 OrdersMapper 接口中添加
    List<StatVo> countAllStatus(); // 管理员用

    List<StatVo> countUserCategory(Integer userId); // 用户用

    List<StatVo> countUserStatus(Integer userId); // 用户用

    Double totalSpentByUser(Integer userId);

    Long countPendingOrders(Integer userId);

    @Select("select count(*) from orders where goods_id = #{goodsId}")
    int countByGoodsId(Integer goodsId);

    @Select("select count(*) from orders where user_id = #{userId}")
    int countByUserId(Integer userId);
}
