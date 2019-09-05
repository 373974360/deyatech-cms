#!/bin/sh
DATA5=`date -d "5 days ago" +%Y%m%d`
DATA2=`date -d "2 days ago" +%Y%m%d`
echo "$DATA5"
rm -rf /backup/database/"$DATA5"*
echo "$DATA2"
rm -rf /backup/upload/"$DATA2"*



