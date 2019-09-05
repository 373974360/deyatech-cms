#!/bin/sh
backup_sign="$(date +"%Y%m%d-%H%M%S")";
zip -r /backup/upload/"$backup_sign".zip /deya/data/nginx/html/upload/ && scp /backup/upload/"$backup_sign".zip root@172.16.101.6:/backup/upload-101.2
