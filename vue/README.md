# 农产品销售系统前端

这是农产品销售系统的前端项目，基于 Vue 3、Vite、Element Plus 和 ECharts 开发。

## 环境要求

- Node.js 18 或更高版本
- npm
- 后端服务默认运行在 `http://localhost:9090`

## 快速启动

```bash
npm install
npm run dev
```

启动后访问终端输出的本地地址，通常是：

```text
http://127.0.0.1:5173/
```

## 接口地址配置

前端接口地址通过 `VITE_BASE_URL` 配置。

开发环境配置文件：

```text
.env.development
```

生产构建配置文件：

```text
.env.production
```

如果部署到服务器，请把 `.env.production` 里的地址改成真实后端地址，例如：

```env
VITE_BASE_URL='http://你的服务器IP:9090'
```

也可以参考 `.env.example` 新建自己的环境配置。

## 常用命令

```bash
npm run dev
npm run build
npm run preview
```

## 默认测试账号

需要后端、MySQL 和 Redis 同时启动后才能登录。

```text
管理员：admin / admin123
普通用户：demo_user / user123
```
