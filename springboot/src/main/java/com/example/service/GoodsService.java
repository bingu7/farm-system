package com.example.service;

import com.example.entity.Goods;
import com.example.exception.CustomException;
import com.example.mapper.GoodsMapper;
import com.example.mapper.GoodsStockMapper;
import com.example.mapper.OrdersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 业务处理
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private GoodsStockMapper goodsStockMapper;

    /**
     * 新增
     */
    public void add(Goods goods) {
        validateGoods(goods);
        goodsMapper.insert(goods);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        if (ordersMapper.countByGoodsId(id) > 0) {
            throw new CustomException("该商品已有订单，不能删除");
        }
        if (goodsStockMapper.countByGoodsId(id) > 0) {
            throw new CustomException("该商品已有库存记录，不能删除");
        }
        goodsMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Goods goods) {
        validateGoods(goods);
        goodsMapper.updateById(goods);
    }

    /**
     * 根据ID查询
     */
    public Goods selectById(Integer id) {
        return goodsMapper.selectById(id);
    }

    public boolean decreaseStock(Integer goodsId, Integer num) {
        return goodsMapper.decreaseStock(goodsId, num) > 0;
    }

    public void increaseStock(Integer goodsId, Integer num) {
        goodsMapper.increaseStock(goodsId, num);
    }

    /**
     * 查询所有
     */
    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    /**
     * 分页查询
     */
    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

    private void validateGoods(Goods goods) {
        if (goods == null) {
            throw new CustomException("商品信息不能为空");
        }
        if (goods.getName() == null || goods.getName().trim().isEmpty()) {
            throw new CustomException("商品名称不能为空");
        }
        if (goods.getPrice() == null || goods.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException("商品价格不能小于0");
        }
        if (goods.getStore() == null || goods.getStore() < 0) {
            throw new CustomException("商品库存不能小于0");
        }
        if (goods.getCategoryId() == null) {
            throw new CustomException("请选择商品分类");
        }
    }

}
