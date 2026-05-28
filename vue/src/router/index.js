import {createRouter, createWebHistory} from 'vue-router'
import { ElMessage } from 'element-plus'

const adminPaths = ['/admin', '/user', '/notice', '/category', '/goods', '/goodsStock']
const userPaths = ['/buy']

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/views/Manager.vue'),
      redirect: '/home',
      children: [
        { path: 'person', component: () => import('@/views/manager/Person.vue')},
        { path: 'password', component: () => import('@/views/manager/Password.vue')},
        { path: 'home', component: () => import('@/views/manager/Home.vue')},
        { path: 'admin', component: () => import('@/views/manager/Admin.vue')},
        { path: 'notice', component: () => import('@/views/manager/Notice.vue')},
        { path: 'category', component: () => import('@/views/manager/Category.vue')},
        { path: 'goods', component: () => import('@/views/manager/Goods.vue')},
        { path: 'goodsStock', component: () => import('@/views/manager/GoodsStock.vue')},
        { path: 'user', component: () => import('@/views/manager/User.vue')},
        { path: 'buy', component: () => import('@/views/manager/Buy.vue')},
        { path: 'orders', component: () => import('@/views/manager/Orders.vue')},
        { path: 'statistics', meta: { name: '数据统计' }, component: () => import('@/views/manager/Statistics.vue')},
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue')},
    { path: '/register', component: () => import('@/views/Register.vue')},
  ]
})

router.beforeEach((to) => {
  if (to.path === '/login' || to.path === '/register') {
    return true
  }

  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user?.token) {
    ElMessage.error('请先登录')
    return '/login'
  }

  if (adminPaths.includes(to.path) && user.role !== 'ADMIN') {
    ElMessage.error('无权限访问')
    return '/home'
  }

  if (userPaths.includes(to.path) && user.role !== 'USER') {
    ElMessage.error('无权限访问')
    return '/home'
  }

  return true
})

export default router
