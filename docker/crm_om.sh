#!/bin/bash
current_datetime=$(date +'%Y%m%d%H%M%S')

# 定义选择菜单函数
select_menu() {
    local options=("$@")
    local selected_option

    PS3="请输入选项编号: "
    select selected_option in "${options[@]}"; do
        if [[ -n $selected_option ]]; then
            echo "$selected_option"
            break
        else
            echo "无效的选择，请重新选择!"
            exit 1
        fi
    done
}

echo "请选择运行系统架构:"
arch_options=("amd64" "arm64" "all")
case $(select_menu "${arch_options[@]}") in
  "amd64") arch="linux/amd64" ;;
  "arm64") arch="linux/arm64" ;;
  "all") arch="linux/amd64,linux/arm64" ;;
  *) echo "无效的值!" && exit 1 ;;
esac

echo "请选择构建镜像的工程:"
env_options=("frontend" "backend" "all")
selected_env=$(select_menu "${env_options[@]}")

## 1.打包后台工程 ###
if [ "$selected_env" = "backend" ] || [ "$selected_env" = "all" ]; then
  echo "[crm_om.sh] 正在进入后台工程目录..."
  cd ../crm-om-server

  echo "[crm_om.sh] 开始打包后台项目到本地..."
  mvn clean package -Dmaven.test.skip=true

  echo "[crm_om.sh] 开始复制后台文件..."
  rm -rf ../docker/crm-om-server/om-web-1.0.0-SNAPSHOT.jar
  scp ../crm-om-server/crm-om-web/target/om-web-1.0.0-SNAPSHOT.jar ../docker/crm-om-server/
  echo "[crm_om.sh] 后台文件复制完成!"

  echo "[crm_om.sh] 开始构建后台镜像..."
  docker buildx build --platform=$arch -t crm-om-server:$current_datetime ../docker/crm-om-server
  if [ $? -ne 0 ]; then
      echo "构建镜像失败!"
      exit 1
  fi
fi

#### 2.打包前台工程 ###
if [ "$selected_env" = "frontend" ] || [ "$selected_env" = "all" ]; then
  echo "[crm_om.sh] 正在进入前台工程目录..."
  cd ../crm-om-web

  echo "[crm_om.sh] 开始打包前台项目到本地..."
  pnpm run build

  echo "[crm_om.sh] 开始复制前台文件..."
  rm -rf ../docker/crm-om-web/dist
  scp -r ../crm-om-web/dist ../docker/crm-om-web
  echo "[crm_om.sh] 前台文件复制完成!"

  echo "[crm_om.sh] 开始构建后台镜像..."
  docker buildx build --platform=linux/arm64 -t crm-om-web:$current_datetime ../docker/crm-om-web
  if [ $? -ne 0 ]; then
      echo "构建镜像失败!"
      exit 1
  fi
fi

#### 3.是否使用服务编排
echo "[crm_om.sh] 是否使用服务编排(y/n)?"
read user_input
if [[ $user_input == "y" || $user_input == "Y" ]]; then
  docker compose up -d
else
  echo "[crm_om.sh] 无需服务编排!"
  exit 1
fi