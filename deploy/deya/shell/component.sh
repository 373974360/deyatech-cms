#!/usr/bin/env bash
#创建自定义网络
docker network create deyatech
#启动consul，并且将默认配置文件挂载到docker容器中
docker run -d --restart=always -m=512m --name consul --network deyatech -p 18500:8500 --privileged=true -v /deya/data/consul/consul_kv.json:/consul/kv/consul_kv.json:ro  -e 'CONSUL_LOCAL_CONFIG={"skip_leave_on_interrupt": true}' consul agent -server -bind=127.0.0.1  -bootstrap-expect=1 -ui -client 0.0.0.0
#启动redis，并且设置自定义密码
docker run -d --restart=always -m=512m  --name redis --network deyatech -p 127.0.0.1:6379:6379 redis redis-server --requirepass "dyt#88352636" --appendonly yes
#启动mysql，设置自定义密码、时区等
docker run -d --restart=always -m=1800m  --name mysql --network deyatech -e MYSQL_ROOT_PASSWORD=dyt@88352636 -e TZ=Asia/Shanghai -p 127.0.0.1:3306:3306 mysql --default-authentication-plugin=mysql_native_password  --default-time-zone='+08:00'
#启动rabbitmq，并且设置默认用户名和密码
docker run -d --restart=always -m=1g --name rabbitmq --network deyatech -e RABBITMQ_DEFAULT_USER=deyatech -e RABBITMQ_DEFAULT_PASS=88352636 rabbitmq:3-management
#启动nginx，挂载默认配置，并且开放80端口
docker run -d --restart=always -m=512m --name nginx --network deyatech -p 80:80 --privileged=true -v /deya/data/nginx/conf/nginx.conf:/etc/nginx/nginx.conf:ro -v /deya//data/nginx/html:/usr/share/nginx/html:ro -v /deya/logs/nginx:/var/log/nginx nginx
#导入consul配置
docker exec -d consul consul kv import --http-addr=http://127.0.0.1:8500  @/consul/kv/consul_kv.json



#创建elk所需要的exchange、queue
docker exec -d rabbitmq rabbitmqadmin -u deyatech -p 88352636 declare exchange name=logs-exchange type=direct durable=true
docker exec -d rabbitmq rabbitmqadmin -u deyatech -p 88352636 declare queue name=logs-queue
docker exec -d rabbitmq rabbitmqadmin -u deyatech -p 88352636 declare binding source=logs-exchange destination=logs-queue routing_key=logs-queue


docker run -d --restart=always -m=512m --name nginx --network deyatech -p 80:80 --privileged=true -v /deya/data/nginx/conf/nginx.conf:/etc/nginx/nginx.conf:ro -v /deya/data/nginx/html:/usr/share/nginx/html:ro -v /deya/data/nginx/conf/nginx.d:/etc/nginx/conf.d:ro -v /deya/vhost:/deya/vhost:ro -v /deya/logs/nginx:/var/log/nginx nginx















##创建自定义网络
#docker network create deyatech
##启动consul，并且将默认配置文件挂载到docker容器中
#docker run -d --restart=always -m=256m --name consul --network deyatech --privileged=true -p 8500:8500 -v /deya/data/consul/consul_kv.json:/consul/kv/consul_kv.json:ro  -e 'CONSUL_LOCAL_CONFIG={"skip_leave_on_interrupt": true}' consul agent -server -bind=127.0.0.1  -bootstrap-expect=1 -ui -client 0.0.0.0
##启动redis，并且设置自定义密码
#docker run -d --restart=always -m=128m  --name redis --network deyatech -p 6379:6379 redis redis-server --requirepass "dyt#88352636"
##启动mysql，设置自定义密码、时区等
#docker run -d --restart=always -m=256m  --name mysql --network deyatech -e MYSQL_ROOT_PASSWORD=dyt@88352636 -e TZ=Asia/Shanghai -p 3306:3306 mysql --default-authentication-plugin=mysql_native_password  --default-time-zone='+08:00'
##启动rabbitmq，并且设置默认用户名和密码
#docker run -d --restart=always -m=256m --name rabbitmq --network deyatech -p 15672:15672 -p 5672:5672  -e RABBITMQ_DEFAULT_USER=deyatech -e RABBITMQ_DEFAULT_PASS=88352636 rabbitmq:3-management
##启动nginx，挂载默认配置，并且开放80端口
#docker run -d --restart=always -m=128m --name nginx --network deyatech -p 80:80 --privileged=true -v /deya/data/nginx/conf/nginx.conf:/etc/nginx/nginx.conf:ro -v /deya//data/nginx/html:/usr/share/nginx/html:ro -v /deya/logs/nginx:/var/log/nginx nginx
##导入consul配置
#docker exec -d consul consul kv import --http-addr=http://127.0.0.1:8500  @/consul/kv/consul_kv.json


## elasticsearch 安装
docker pull docker.elastic.co/elasticsearch/elasticsearch:6.2.4
docker run -d --name elasticsearch --network deyatech -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.2.4
#安装分词插件  https://github.com/medcl/elasticsearch-analysis-ik
docker exec -it elasticsearch bash
cd /usr/share/elasticsearch/bin
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.2.4/elasticsearch-analysis-ik-6.2.4.zip
#安装拼音插件  https://github.com/medcl/elasticsearch-analysis-pinyin
docker exec -it elasticsearch bash
cd /usr/share/elasticsearch/bin
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-pinyin/releases/download/v6.2.4/elasticsearch-analysis-pinyin-6.2.4.zip