FROM openjdk:21-jdk
LABEL authors="zhangxiaojun"

# 设置工作目录
WORKDIR /usr/src/crm-om-server
COPY om-web-1.0.0-SNAPSHOT.jar /usr/src/crm-om-server/crm-om-server-1.0.jar
EXPOSE 8848
ENTRYPOINT ["java -jar", "crm-om-server-1.0.jar"]