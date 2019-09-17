#!/bin/sh
DATE5=`date -d "5 days ago" +%Y-%m-%d`
DATE=`date +%Y-%m-%d`
echo "$DATE5"
rm -rf /deya/logs/app/*"$DATE5"*

#打包nginx日志
zip /deya/logs/nginx/access.log."$DATE".zip /deya/logs/nginx/access.log
zip /deya/logs/nginx/error.log."$DATE".zip /deya/logs/nginx/error.log

#重置nginx日志
echo ""> /deya/logs/nginx/access.log
echo ""> /deya/logs/nginx/error.log

rm -rf /deya/logs/nginx/*"$DATE5"*