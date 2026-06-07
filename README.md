# 农产品供销系统

这是一个前后端分离的农产品供销系统项目。

项目结构：

```text
farm_system/
├─ mysql/       数据库建表与初始化脚本
├─ vue/         前端项目，基于 Vue 3 + Vite + Element Plus
└─ springboot/  后端项目，基于 Spring Boot + MyBatis + MySQL + Redis
```

## 前端启动

```bash
cd vue
npm install
npm run dev
```

默认访问地址：

```text
http://127.0.0.1:5173/
```

## 后端启动

```bash
cd springboot
mvn dependency:build-classpath "-Dmdep.outputFile=target/classpath.txt" "-Dmdep.includeScope=runtime"
```

Windows PowerShell 下启动：

```powershell
$cp = "target\classes;" + (Get-Content target\classpath.txt -Raw)
java -cp $cp com.example.SpringbootApplication
```

默认后端地址：

```text
http://localhost:9090
```

## 运行依赖

- MySQL
- Redis
- Node.js 18+
- JDK 21
- Maven 3.9+

## 数据库初始化

数据库脚本位于：

```text
mysql/farm_system.sql
```

先创建名为 `farm_system` 的数据库，再导入该脚本即可。

后端默认从环境变量读取数据库和 Redis 配置，可按需设置：

```text
DB_USERNAME
DB_PASSWORD
DB_HOST
DB_PORT
DB_NAME
REDIS_HOST
REDIS_PORT
REDIS_PASSWORD
FILE_BASE_URL
JWT_SECRET
```

`JWT_SECRET` 建议设置为至少 32 位字符。默认值仅用于本地开发，生产部署必须覆盖。

## 默认测试账号

```text
管理员：admin / admin123
普通用户：ccc / 123
```
## 上传目录配置

上传图片保存目录通过 `FILE_UPLOAD_DIR` 配置。建议使用绝对路径，例如：

```powershell
$env:FILE_UPLOAD_DIR="D:\farm-system\uploads"
```

如果不设置，后端默认保存到 `D:/farm-system/uploads`。生产或多人协作环境建议显式配置 `FILE_UPLOAD_DIR`，避免上传文件分散到不同目录。
