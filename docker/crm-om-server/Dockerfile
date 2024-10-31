FROM eclipse-temurin:21-jdk-alpine
LABEL authors="zhangxiaojun"

# 设置工作目录
WORKDIR /usr/src/crm-om-server
COPY om-web-1.0.0-SNAPSHOT.jar /usr/src/crm-om-server/crm-om-server-1.0.jar
EXPOSE 8848
ENTRYPOINT ["java", "-jar", "crm-om-server-1.0.jar", "--spring.profiles.active=prod", "-c"]

# 启动
#docker run -itd --name="crm-om-server" -p 8848:8848 crm-om-server:20241026152148
