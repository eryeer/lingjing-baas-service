#!/bin/bash
source /home/ubuntu/.conf
cd /opt/baas

JDBC_URL="jdbc:mysql://124.70.219.113:3306/dna2_explorer?characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&rewriteBatchedStatements=true"
nohup java -jar -DENV=dev \
-DPORT=8702 \
-DMYSQL_URL=${JDBC_URL} \
-DMYSQL_USERNAME=root \
-DMYSQL_PASSWORD=root \
-DREDIS_HOST=124.70.219.113 \
-DREDIS_PASSWORD=password \
-DREDIS_PORT=6379 \
-DNODE_URL=http://124.70.219.113:8545 \
dna2-explorer.jar > Explorer.log 2>&1  &
