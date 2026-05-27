package com.example.entity;

/**
 * 专门用于 Echarts 统计的实体类
 */
public class StatVo {
    private String name;  // 对应图表的分类名称（如：水果、待支付）
    private Long value;   // 对应图表的数据数值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}