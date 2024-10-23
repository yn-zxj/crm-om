#!/bin/bash
current_datetime=$(date +'%Y%m%d%H%M%S')

### 1.打包后台工程 ###
echo "[crm_om.sh] 正在进入后台工程目录..."
cd ../crm-om-server

echo "[crm_om.sh] 开始打包后台项目到本地..."
mvn clean package -Dmaven.test.skip=true

echo "[crm_om.sh] 开始复制后台文件..."
rm -rf ../docker/crm-om-server/om-web-1.0.0-SNAPSHOT.jar
scp ../crm-om-server/crm-om-web/target/om-web-1.0.0-SNAPSHOT.jar ../docker/crm-om-server/
echo "[crm_om.sh] 后台文件复制完成!"

echo "[crm_om.sh] 开始构建后台镜像..."
docker buildx build --platform=linux/amd64 -t crm-om-server:$current_datetime ../docker/crm-om-server
if [ $? -ne 0 ]; then
    echo "构建镜像失败!"
    exit 1
fi

### 2.打包前台工程 ###
echo "[crm_om.sh] 正在进入前台工程目录..."
cd ../crm-om-web

echo "[crm_om.sh] 开始打包前台项目到本地..."
pnpm run build

echo "[crm_om.sh] 开始复制前台文件..."
rm -rf ../docker/crm-om-web/dist
scp -r ../crm-om-web/dist ../docker/crm-om-web
echo "[crm_om.sh] 前台文件复制完成!"
