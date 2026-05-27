<template>
  <div style="padding-bottom: 50px">
    <div class="card" style="margin-bottom: 10px">
      <el-button :class="{ 'active' : data.activeCategoryId === null }" @click="loadCategoryGoods(null)">全部</el-button>
      <el-button :class="{ 'active' : data.activeCategoryId === item.id }" @click="loadCategoryGoods(item.id)" v-for="item in data.categoryList" :key="item.id">{{ item.name }}</el-button>
    </div>
    <div style="margin-bottom: 10px">
      <el-input style="width: 300px; margin-right: 5px" v-model="data.name" placeholder="请输入商品名称关键字查询"></el-input>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-row :gutter="10" v-if="data.total > 0">
     <el-col style="margin-bottom: 10px" :span="6" v-for="item in data.goodsList" :key="item.id">
      <div class="card">
        <img :src="item.img" alt="" style="width: 100%; height: 280px">
        <div style="font-size: 18px; margin: 5px 0; color: #333">{{item.name}}</div>
        <el-tooltip v-if="item.descr && item.descr.length > 40" :content="item.descr" effect="light" placement="top">
          <div class="line2" style="margin: 5px 0; color: #666; font-size: 14px; height: 40px">{{ item.descr }}</div>
        </el-tooltip>
        <div v-else class="line2" style="margin: 5px 0; color: #666; font-size: 14px; height: 40px">{{ item.descr }}</div>
        <div style="margin: 5px 0">
          <el-tag type="success">{{ item.specials }}</el-tag>
        </div>
        <div style="margin: 10px 0; display: flex; align-items: center; color: #666">
          <div style="flex: 1">
            <strong style="color: red; font-size: 18px">￥{{ item.price }}</strong>/{{ item.unit }}
          </div>
          <div style="flex: 1; text-align: center">
            库存：{{ item.store }}
          </div>
          <div>
            <el-input-number @change="handleBuy(item)" v-model="item.num" style="width: 120px" :min="0" :max="item.store"></el-input-number>
          </div>
        </div>
        <div style="display: flex; align-items: center; justify-content: flex-end" v-if="item.num > 0">
          总价：<strong style="margin-right: 5px; display: inline-block; min-width: 50px; font-size: 18px; color: red">￥{{ item.total }}</strong>
          <el-button type="primary" @click="buy(item)">购买</el-button>
        </div>
      </div>
     </el-col>
    </el-row>
    <div style="padding: 50px 0; text-align: center; font-size: 24px; color: #888" v-else>暂无农产品...</div>

    <div style="margin-top: 10px" v-if="data.total">
      <el-pagination layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" @current-change="load" :total="data.total"/>
    </div>

  </div>
</template>

<script setup>
  import { reactive } from "vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";

  const data = reactive({
    user: JSON.parse(localStorage.getItem('system-user') || '{}'),
    categoryList: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    goodsList: [],
    name: '',
    activeCategoryId: null  // 当前选中的分类ID
  })

  const buy = (goods) => {
    let orderData = { goodsId: goods.id, num: goods.num, userId: data.user.id }
    request.post('/orders/add', orderData).then(res => {
      if (res.code === '200') {
        ElMessage.success('购买成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }

  const handleBuy = (goods) => {
    goods.total = (goods.price * goods.num).toFixed(2)  // 计算总价  保留2位小数
  }

  // 获取到分类的数据
  request.get('/category/selectAll').then(res => {
    data.categoryList = res.data
  })

  // 分页查询
  const load = () => {
    request.get('/goods/selectPage', {
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        name: data.name,
        categoryId: data.activeCategoryId
      }
    }).then(res => {
      data.goodsList = res.data?.list
      data.total = res.data?.total
      data.goodsList.forEach(item => {
        item.num = 0
      })
    })
  }

  const loadCategoryGoods = (categoryId) => {
    data.activeCategoryId = categoryId
    data.pageNum = 1
    load()
  }

  const handleSearch = () => {
    data.pageNum = 1
    load()
  }

  load()
</script>

<style>
.active {
  color: white !important;
  background-color: #1967e3 !important;
}
</style>
