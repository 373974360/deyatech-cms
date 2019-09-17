#!/usr/bin/env bash
#安装依赖包、java、docker
yum -y install gcc gcc-c++ libXss* java docker
#安装dokcer
service docker start
#docker启动报错解决：
# vi /etc/sysctl.conf
# 添加 一行 vm.max_map_count=655360
# 加载参数
# sysctl -p
#设置docker开机启动
#cat /etc/sysconfig/docker
#没有启动新的内核，修改的docker配置文件。将配置文件的“--selinux-enabled”改成“--selinux-enabled=false”，然后再重启docker。
chkconfig docker on
systemctl stop docker.service
systemctl start docker.service
systemctl enable docker.service
#安装docker-compose
#sudo curl -L "https://github.com/docker/compose/releases/download/1.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#sudo chmod +x /usr/local/bin/docker-compose
#sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

#查看各docker容器内存、CPU使用情况
#docker stats $(docker ps --format={{.Names}})

#定时任务设置  crontab -e
#0 7 * * * /deya/shell/backup-db.sh
#0 19 * * * /deya/shell/backup-db.sh
#0 8 * * * /deya/shell/backup-upload.sh
#0 20 * * * /deya/shell/backup-upload.sh
#0 0 * * * /deya/shell/clear-backup.sh
#0 0 * * * /deya/shell/clear-logs.sh


#docker容器下载加速器

sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://pcdrqwiu.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker