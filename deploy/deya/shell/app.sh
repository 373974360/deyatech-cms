#!/bin/bash
#自定义命令列表
command_list[0]="docker-pull 从远程拉取images"
command_list[1]="docker-run 根据已有的image启动服务"
command_list[2]="docker-pull-and-run 从远程拉取images并且启动服务"
command_list[3]="docker-restart 重新启动服务"
#自定义服务列表
server_list[0]=common-service
server_list[1]=admin-service
server_list[2]=gateway
server_list[3]=workflow-service
server_list[4]=content-service
server_list[5]=interview-service
server_list[6]=monitor-service
server_list[7]=resource-service
server_list[8]=setting-service
server_list[9]=station-service
server_list[10]=statistics-service
server_list[11]=template-service
server_list[12]=appeal-service
server_list[13]=assembly-service
#阿里云私有docker仓库地址
docker_server=registry.cn-hangzhou.aliyuncs.com/deyatech/
#image版本号
today=`date +%Y%m%d`
#docker容器内存限制大小
xxm=400
#打印命令提示
echo -e "\e[1;31m"	#高亮绿色显示
printf "%s\n" "命令列表如下："
echo -e "\e[1;32m"	#高亮绿色显示
for i in "${!command_list[@]}";
do
    printf "\t%s、%s\n" "$i" "${command_list[$i]}"
done
#打印服务提示
echo -e "\e[0m"	#恢复打印颜色
echo -e "\e[1;31m"	#高亮绿色显示
printf "%s\n" "服务列表如下："
echo -e "\e[1;32m"	#高亮绿色显示
for i in "${!server_list[@]}";
do
    printf "\t%s、%s\n" "$i" "${server_list[$i]}"
done
echo -e "\e[0m"	#恢复打印颜色

process(){
    if [ -z "${command}" ]; then
        echo -e "\e[1;31m"	#高亮绿色显示
        printf "%s\n" "请按照格式输入命令"
        echo -e "\e[0m"	#恢复打印颜色
        exit
    fi
    if [ -z "${index}" ]; then
        case "$command" in
            "0")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "从远程拉取所有服务镜像文件"
                echo -e "\e[0m"	#恢复打印颜色
                for index in "${!server_list[@]}";
                do
                    rmi
                    pull
                done
                ;;
            "1")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "用本地镜像启动所有服务对应的docker容器"
                echo -e "\e[0m"	#恢复打印颜色
                for index in "${!server_list[@]}";
                do
                    rm-f
                    run
                done
                ;;
            "2")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "从远程拉取所有服务镜像文件并启动对应的docker容器"
                echo -e "\e[0m"	#恢复打印颜色
                for index in "${!server_list[@]}";
                do
                    rm-f
                    rmi
                    run
                done
                ;;
            "3")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "重新启动所有服务对应的docker容器"
                echo -e "\e[0m"	#恢复打印颜色
                for index in "${!server_list[@]}";
                do
                    restart
                done
                ;;
            *)
                error
                ;;
        esac
    else
        case "$command" in
            "0")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "从远程拉取${server_list[$index]}服务镜像文件"
                echo -e "\e[0m"	#恢复打印颜色
                rmi
                pull
                ;;
            "1")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "用本地镜像启动${server_list[$index]}对应的docker容器"
                echo -e "\e[0m"	#恢复打印颜色
                rm-f
                run
                ;;
            "2")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "从远程拉取所有${server_list[$index]}镜像文件并启动对应的docker容器"
                echo -e "\e[0m"	#恢复打印颜色
                rm-f
                rmi
                run
                ;;
            "3")
                echo -e "\e[1;32m"	#高亮绿色显示
                printf "%s\n" "重新启动${server_list[$index]}对应的docker容器"
                echo -e "\e[0m"	#恢复打印颜色
                restart
                ;;
            *)
                error
                ;;
        esac
    fi
}

pull(){
    if [ "${index}" -lt "4" ];then
        version=master-${today}
	  else
		    version=master-${today}
    fi
    docker pull ${docker_server}${server_list[$index]}:${version}
    echo -e "\e[1;32m"	#高亮绿色显示
    printf "%s\n" "docker pull ${docker_server}${server_list[$index]}:${version} Success!"
    echo -e "\e[0m"	#恢复打印颜色
}

rm-f(){
    docker rm -f ${server_list[$index]}
    echo -e "\e[1;32m"	#高亮绿色显示
    printf "%s\n" "docker rm -f ${server_list[$index]} Success!"
    echo -e "\e[0m"	#恢复打印颜色
}

rmi(){
    docker images|grep ${server_list[$index]}|awk '{print $3}'|xargs docker rmi
    echo -e "\e[1;32m"	#高亮绿色显示
    printf "%s\n" "docker rmi ${server_list[$index]} Success!"
    echo -e "\e[0m"	#恢复打印颜色
}

run(){
    upload_path=""
    vhost_path=""
    nginx_path=""
    if [ "${index}" = "0" ];then
        upload_path=" -v /deya/data/nginx/html/upload/:/deya/upload/ "
    fi
    if [ "${index}" = "11" -o "${index}" = "9" -o "${index}" = "7" ];then
        vhost_path=" -v /deya/vhost/:/deya/vhost/ "
    fi
    if [ "${index}" = "7"  ];then
        nginx_path=" -v /deya/data/nginx/conf/nginx.d/:/deya/data/nginx/conf/nginx.d/ "
    fi
    if [ "${index}" -lt "4" ];then
        version=master-${today}
	  else
		    version=master-${today}
    fi
    docker run -d --restart=always --name ${server_list[$index]} --network deyatech --privileged=true -v /deya/logs/${server_list[$index]}/:/deya/logs/app/ ${upload_path} ${vhost_path} ${nginx_path} -m=${xxm}m --oom-kill-disable  ${docker_server}${server_list[$index]}:${version}
    echo -e "\e[1;32m"	#高亮绿色显示
    printf "%s\n" "docker run ${server_list[$index]} Success!"
    echo -e "\e[0m"	#恢复打印颜色
}

restart(){
    docker restart ${server_list[$index]}
    echo -e "\e[1;32m"	#高亮绿色显示
    printf "%s\n" "docker restart ${server_list[$index]} Success!"
    echo -e "\e[0m"	#恢复打印颜色
}

#命令错误提示
error(){
	echo -e "\e[1;31m"	#高亮红色显示
	printf "%s\n" "输入的命令不存在！"
	echo -e "\e[0m"	#恢复打印颜色
}

if [ $# -gt 0 ];then
	command=$1
	index=$2
	process
else
    echo -e "\e[1;31m"	#高亮红色显示
    read -t 30 -p "请输入选择的命令（1 3 如果第二个参数为空，则为所有服务）:" command index
    echo -e "\e[0m"	#恢复打印颜色
    process
fi