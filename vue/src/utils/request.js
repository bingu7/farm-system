import { ElMessage } from 'element-plus'
import router from '../router'
import axios from 'axios'

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000
})

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'

    const user = JSON.parse(localStorage.getItem('system-user') || '{}')
    if (user.token) {
        config.headers.token = user.token
    }

    return config
}, error => {
    return Promise.reject(error)
})

const handleBusinessResult = (res) => {
    if (!res || typeof res !== 'object') {
        return res
    }

    if (res.code === '401') {
        ElMessage.error(res.msg || '请重新登录')
        localStorage.removeItem('system-user')
        router.push('/login')
        return res
    }

    if (res.code === '403') {
        ElMessage.error(res.msg || '无权限操作')
        return res
    }

    if (res.code && res.code !== '200') {
        ElMessage.error(res.msg || '请求失败')
    }

    return res
}

request.interceptors.response.use(
    response => {
        let res = response.data
        if (response.config.responseType === 'blob') {
            return res
        }
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return handleBusinessResult(res)
    },
    error => {
        const res = error.response?.data
        if (res) {
            return handleBusinessResult(typeof res === 'string' ? JSON.parse(res) : res)
        }

        ElMessage.error('网络连接异常，请稍后再试')
        return Promise.reject(error)
    }
)

export default request
