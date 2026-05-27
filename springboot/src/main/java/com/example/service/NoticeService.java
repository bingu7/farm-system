package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 系统公告业务处理
 **/
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 定义缓存的 Key
    private static final String NOTICE_CACHE_KEY = "CACHE_NOTICE_ALL";

    /**
     * 新增
     */
    public void add(Notice notice) {
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
        // 只要数据变动，就清除缓存
        clearCache();
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
        // 只要数据变动，就清除缓存
        clearCache();
    }

    /**
     * 修改
     */
    public void updateById(Notice notice) {
        noticeMapper.updateById(notice);
        // 只要数据变动，就清除缓存
        clearCache();
    }

    /**
     * 根据ID查询
     */
    public Notice selectById(Integer id) {
        return noticeMapper.selectById(id);
    }

    /**
     * 查询所有（带缓存逻辑）
     */
    public List<Notice> selectAll(Notice notice) {
        // 1. 如果是带条件的查询（比如搜标题），通常不走这个全局缓存，直接查库
        if (notice != null && notice.getTitle() != null) {
            return noticeMapper.selectAll(notice);
        }

        // 2. 尝试从 Redis 获取全局公告列表
        List<Notice> list = (List<Notice>) redisTemplate.opsForValue().get(NOTICE_CACHE_KEY);

        if (list == null) {
            // 3. 缓存没有，查数据库
            list = noticeMapper.selectAll(notice);
            // 4. 存入 Redis，并设置过期时间为 10 分钟（防止极端情况下数据不一致）
            redisTemplate.opsForValue().set(NOTICE_CACHE_KEY, list, 10, TimeUnit.MINUTES);
            System.out.println(">>> [Redis] 缓存未命中，从数据库加载公告列表");
        } else {
            System.out.println(">>> [Redis] 缓存命中，直接返回公告列表");
        }
        return list;
    }

    /**
     * 分页查询
     */
    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }

    /**
     * 提取一个私有方法，专门负责清理缓存
     */
    private void clearCache() {
        redisTemplate.delete(NOTICE_CACHE_KEY);
        System.out.println(">>> [Redis] 检测到数据变动，已清理公告缓存");
    }

}