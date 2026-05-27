import { ElMessage } from 'element-plus'
import router from '../router'
import axios from "axios";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000  // 后台接口超时时间设置
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    
    // ======= 【新增：注入 Token 逻辑】 =======
    // 从本地存储读取登录时保存的用户信息
    let user = JSON.parse(localStorage.getItem("system-user") || '{}');
    if (user.token) {
        // 在请求头中添加 token 字段，后端 JwtInterceptor 会拦截这个字段
        config.headers['token'] = user.token; 
    }
    // =========================================
    
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 当权限验证不通过的时候给出提示
        
        // ======= 【修改：细化业务错误码处理】 =======
        // 1. 如果 code 是 401，说明权限验证失败（未登录或 Token 过期）
        if (res.code === '401') {
            ElMessage.error(res.msg || '请重新登录');
            router.push("/login") // 跳转到登录页
            return res; // 返回 res 阻止后续逻辑
        }
        
        // 2. 如果 code 是 500，说明是业务报错（比如：用户已存在、密码错误等）
        if (res.code === '500') {
            ElMessage.error(res.msg); // 只弹框提示错误，不跳转页面
            // 注意：这里不需要跳转
        }
        // =========================================

        return res;
    },
        error => {
        console.log('err' + error)
        // 处理 HTTP 状态码错误（如 404, 502 等）
        ElMessage.error("网络连接异常，请稍后再试");
        return Promise.reject(error)
    }
)


export default request
