<template>
  <div>
    <!-- 顶部卡片 -->
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="8" v-for="i in 3" :key="i">
        <div class="statistics-card" :class="'bg-' + i">
          <div class="title">{{ data.stat['card' + i + 'Text'] }}</div>
          <div class="number">{{ data.stat['card' + i] || 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表展示 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card">
          <div id="pieChart" style="width: 100%; height: 450px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card">
          <div id="barChart" style="width: 100%; height: 450px"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request";
import * as echarts from 'echarts';

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  stat: {}
})

const initCharts = (statData) => {
  // 1. 饼图配置
  const pieDom = document.getElementById('pieChart');
  const pieChart = echarts.init(pieDom);
  pieChart.setOption({
    title: { text: data.user.role === 'ADMIN' ? '农产品库存类别分布' : '我的购买类别偏好', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { bottom: '5%' },
    series: [{
      name: '数量',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: false, position: 'center' },
      data: statData.pieData
    }]
  });

  // 2. 柱状图配置
  const barDom = document.getElementById('barChart');
  const barChart = echarts.init(barDom);
  barChart.setOption({
    title: { text: '订单状态数量分布统计', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: statData.barData.map(v => v.name) },
    yAxis: { type: 'value' },
    series: [{
      data: statData.barData.map(v => v.value),
      type: 'bar',
      showBackground: true,
      backgroundStyle: { color: 'rgba(180, 180, 180, 0.2)' },
      itemStyle: { color: '#409EFF' }
    }]
  });
}

onMounted(() => {
  request.get('/statistics', {
    params: { userId: data.user.id, role: data.user.role }
  }).then(res => {
    if (res.code === '200') {
      data.stat = res.data
      initCharts(res.data)
    }
  })
})
</script>

<style scoped>
.statistics-card { padding: 30px; border-radius: 10px; color: white; text-align: center; }
.bg-1 { background: linear-gradient(135deg, #409EFF, #79bbff); }
.bg-2 { background: linear-gradient(135deg, #67C23A, #95d475); }
.bg-3 { background: linear-gradient(135deg, #E6A23C, #eebe77); }
.title { font-size: 16px; font-weight: bold; }
.number { font-size: 32px; font-weight: bold; margin-top: 10px; }
.card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1); }
</style>