package com.example.service;

import com.example.entity.Goods;
import com.example.entity.GoodsStock;
import com.example.mapper.GoodsStockMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务处理
 **/
@Service
public class GoodsStockService {

    @Resource
    private GoodsStockMapper goodsStockMapper;

    @Resource
    private GoodsService goodsService;

    /**
     * 新增：增加进货记录，并增加商品库存
     */
    @Transactional
    public void add(GoodsStock goodsStock) {
        // 1. 更新商品表库存
        Goods goods = goodsService.selectById(goodsStock.getGoodsId());
        if (goods != null) {
            goods.setStore(goods.getStore() + goodsStock.getNum());
            goodsService.updateById(goods);
        }
        // 2. 插入进货记录
        goodsStockMapper.insert(goodsStock);
    }

    /**
     * 修改：计算差值，同步更新商品库存
     */
    @Transactional
    public void updateById(GoodsStock goodsStock) {
        // 1. 获取数据库中旧的进货数据
        GoodsStock oldStock = goodsStockMapper.selectById(goodsStock.getId());
        // 2. 获取对应商品
        Goods goods = goodsService.selectById(goodsStock.getGoodsId());

        if (goods != null && oldStock != null) {
            // 3. 计算差值 = 新输入的数量 - 原来的数量
            // 比如原来录入10，现在改15，diff就是5，库存加5
            // 比如原来录入10，现在改8，diff就是-2，库存减2
            int diff = goodsStock.getNum() - oldStock.getNum();

            // 4. 更新商品总库存
            goods.setStore(goods.getStore() + diff);
            goodsService.updateById(goods);
        }

        // 5. 更新进货记录表
        goodsStockMapper.updateById(goodsStock);
    }

    /**
     * 删除：删除进货记录，需同步扣减商品库存
     */
    @Transactional
    public void deleteById(Integer id) {
        GoodsStock goodsStock = goodsStockMapper.selectById(id);
        if (goodsStock != null) {
            Goods goods = goodsService.selectById(goodsStock.getGoodsId());
            if (goods != null) {
                // 扣除这笔进货带来的库存
                goods.setStore(goods.getStore() - goodsStock.getNum());
                goodsService.updateById(goods);
            }
        }
        goodsStockMapper.deleteById(id);
    }

    /**
     * 根据ID查询
     */
    public GoodsStock selectById(Integer id) {
        return goodsStockMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<GoodsStock> selectAll(GoodsStock goodsStock) {
        return goodsStockMapper.selectAll(goodsStock);
    }

    /**
     * 分页查询
     */
    public PageInfo<GoodsStock> selectPage(GoodsStock goodsStock, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsStock> list = goodsStockMapper.selectAll(goodsStock);
        return PageInfo.of(list);
    }

}