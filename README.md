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
作为支持，开源项目作者会对其进行维护更新，但是开发之中想同步最新的代码，此处使用 `git subtree` 进行管理。

① 新增远程仓库 URL

```shell
$ git remote add -f web https://github.com/soybeanjs/soybean-admin.git

# 查看
$ git remote show
> origin
> web
```

② 将 web 项目合并到本地 Git 项目

```shell
$ git subtree add --prefix=crm-om-web web main --squash
> git fetch web main
> From https://github.com/soybeanjs/soybean-admin
>  * branch              main       -> FETCH_HEAD
> Added dir 'crm-om-web'
```

③ 提交更改至主仓库

```shell
$ git push origin main
```

④ 同步更新与修改

```shell
$ git subtree pull --prefix=crm-om-web web main --squash

# 当前无更新
> From https://github.com/soybeanjs/soybean-admin
> * branch              main       -> FETCH_HEAD
>Subtree is already at commit 9c012c7d13516ba52ed7a6acb03b9695d95271b5.
```

⑤ 启动前台

```shell
# 进入前台目录
cd crm-om-web

# 加载依赖
pnpm i

# 运行启动
pnpm run dev
```