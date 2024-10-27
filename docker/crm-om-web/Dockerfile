FROM nginx:alpine AS prod
LABEL authors="zhangxiaojun"

# 设置工作目录
WORKDIR /usr/src/crm-om-web
COPY dist/ /usr/src/crm-om-web/
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

# 启动
#docker run -itd --name="crm-om-web" -p 80:80 crm-om-web:20241026152148