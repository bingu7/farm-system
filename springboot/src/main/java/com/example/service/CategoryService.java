package com.example.service;

import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 业务处理
 **/
@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 1. 定义缓存的 Key
    private static final String CATEGORY_CACHE_KEY = "CACHE_CATEGORY_ALL";

    /**
     * 新增
     */
    public void add(Category category) {
        categoryMapper.insert(category);
        clearCache(); // 清理缓存
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
        clearCache(); // 清理缓存
    }

    /**
     * 修改
     */
    public void updateById(Category category) {
        categoryMapper.updateById(category);
        clearCache(); // 清理缓存
    }

    /**
     * 根据ID查询
     */
    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }

    /**
     * 查询所有分类（带缓存逻辑）
     */
    public List<Category> selectAll(Category category) {
        // 如果是带条件的搜索，直接查数据库，不走全局缓存
        if (category != null && category.getName() != null) {
            return categoryMapper.selectAll(category);
        }

        // 2. 先尝试从 Redis 获取
        List<Category> list = (List<Category>) redisTemplate.opsForValue().get(CATEGORY_CACHE_KEY);

        if (list == null) {
            // 3. 缓存没有，查数据库
            list = categoryMapper.selectAll(category);
            // 4. 存入 Redis，设置 6个小时过期（分类数据变动更少，可以设久一点）
            redisTemplate.opsForValue().set(CATEGORY_CACHE_KEY, list, 6, TimeUnit.HOURS);
            System.out.println(">>> [Redis] 分类缓存未命中，从数据库加载");
        } else {
            System.out.println(">>> [Redis] 分类缓存命中，直接返回");
        }
        return list;
    }

    /**
     * 分页查询
     */
    public PageInfo<Category> selectPage(Category category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }

    /**
     * 辅助方法：清理缓存
     */
    private void clearCache() {
        redisTemplate.delete(CATEGORY_CACHE_KEY);
        System.out.println(">>> [Redis] 分类数据变动，已清理缓存");
    }

}