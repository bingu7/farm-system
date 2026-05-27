package com.example.controller;

import com.example.common.Result;
import com.example.Utils.AuthUtils;
import com.example.entity.GoodsStock;
import com.example.service.GoodsStockService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端操作接口
 **/
@RestController
@RequestMapping("/goodsStock")
public class GoodsStockController {

    @Resource
    private GoodsStockService goodsStockService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody GoodsStock goodsStock) {
        AuthUtils.requireAdmin();
        goodsStockService.add(goodsStock);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        AuthUtils.requireAdmin();
        goodsStockService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody GoodsStock goodsStock) {
        AuthUtils.requireAdmin();
        goodsStockService.updateById(goodsStock);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        AuthUtils.requireAdmin();
        GoodsStock goodsStock = goodsStockService.selectById(id);
        return Result.success(goodsStock);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(GoodsStock goodsStock) {
        AuthUtils.requireAdmin();
        List<GoodsStock> list = goodsStockService.selectAll(goodsStock);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(GoodsStock goodsStock,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        AuthUtils.requireAdmin();
        PageInfo<GoodsStock> page = goodsStockService.selectPage(goodsStock, pageNum, pageSize);
        return Result.success(page);
    }

}
