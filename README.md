#### 一、项目概述

```text
# tree -L 1
crm-om
├── crm-om-server  // 后台项目
├── crm-om-web     // 前台项目
└── README.md      // 说明文档

```

#### 二、项目目录说明

##### crm-om-server

&#8195;&#8195;详情参见文件夹内`README.md`说明！

##### crm-om-web

&#8195;&#8195;前台框架使用了 [Soybean Admin](https://github.com/soybeanjs/soybean-admin.git)
作为支持，开源项目作者会对其进行维护更新，但是开发之中想同步最新的代码，就需要做如下设置：

① 进入前台目录 `cd crm-om-web`

② 查询当前远程仓库

```shell
git remote -v

# 返回
origin  https://github.com/soybeanjs/soybean-admin.git (fetch)
origin  https://github.com/soybeanjs/soybean-admin.git (push)
```

③ 添加自己的 git 源地址

```shell
git remote add om-web https://github.com/yn-zxj/crm-om.git
```

④ 提交代码到自己的 git

```shell
# 往自己仓库推代码
git push om-web main

# 同步最新代码(往本地拉取)
git pull om-web main
```

⑤ 同步开源最新代码

```shell
git pull origin main
```

> 注意：
>
> ① 如有需要先同步开源最新代码，遇到冲突先解决在合并;
> ② 确保远程开源地址正确，且