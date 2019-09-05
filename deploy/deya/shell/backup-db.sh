#!/bin/sh
backup_sign="$(date +"%Y%m%d-%H%M%S")";
docker exec mysql mysqldump -uroot -pdyt@88352636 deyatech > /backup/database/"$backup_sign".sql && zip /backup/database/"$backup_sign".zip /backup/database/"$backup_sign".sql && rm -f /backup/database/"$backup_sign".sql && scp /backup/database/"$backup_sign".zip root@47.96.11.207:/deya/backup/zwfw-lianhu && scp /backup/database/"$backup_sign".zip root@172.16.101.6:/backup/database-101.2
