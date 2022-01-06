#!/bin/bash

export LC_CTYPE=en_US.UTF-8
export LANG=en_US.UTF-8

dos2unix -q ./log.sh
. ./log.sh

## 检测工具dos2unix是否已安装
which dos2unix >/dev/null 2>&1
if [ $? -ne 0 ];then
	LogError "Please install tool [dos2unix]."
	exit 1
fi

ori_globe_conf="./globe.common.conf"
bak_globe_conf="./globe.common.conf.bak"
end_globe_conf="./globe.common.conf.end"

function check()
{
	## 第一个参数：公共配置变量名
	## 第二个参数：私有配置变量名（如果与公共配置一致，可不传）
	if [ $# -eq 1 ]; then
		grep "^$1=" ${ori_globe_conf} >>${bak_globe_conf}
	elif [ $# -eq 2 ]; then
		grep "^$2=" ${ori_globe_conf} >>${bak_globe_conf}
		if [ ! -z ${!1} ];then
			sed -i "/^$2=/ c\$2=${!1}" ${bak_globe_conf}
		fi
	fi
}
	
function check_common()
{
    check JDK_ROOT

    check SPRING_KAFKA_BOOTSTRAP_SERVERS
    check SPRING_KAFKA_MESSAGE_TOPIC

    check ATLANTIS_MESSAGE_LIMIT_APP
    check ATLANTIS_MESSAGE_LIMIT_USER

    check MINIO_HTTPURL
    check MINIO_ENDPOINT
    check MINIO_ACCESSKEY
    check MINIO_SECRETKEY
    check MINIO_BUCKETNAME
    check MINIO_PORT
}

function check_jdk() {

    JDK_ROOT=`cat ${ori_globe_conf}  | grep '^JDK_ROOT' | cut -d= -f2`
    version=`${JDK_ROOT}/bin/java -version 2>&1 | sed '1!d' | sed -e 's/"//g' | awk '{print $3}'`

    result=$(echo $version|grep '1.8')
    if [[ $result == "" ]]
    then
        LogError "前版本 $version非期望版本1.8"
        exit 1;
    else
        LogInfo "当前版本$version符合项目要求"
    fi
}


function check_db()
{
	check MESSAGE_DATASOURCE_URL
    check MESSAGE_DATASOURCE_USERNAME
    check MESSAGE_DATASOURCE_PASSWORD

    check USER_DATASOURCE_URL
    check USER_DATASOURCE_USERNAME
    check USER_DATASOURCE_PASSWORD
}


## 检查配置文件
function check_common_conf()
{
	rm -rf ${bak_globe_conf} ${end_globe_conf}
	
	if [ ! -f ${ori_globe_conf} ];then
		echo -e "\\033[1;31mconfig file ${ori_globe_conf} is not exist.\\033[0;39m"
		exit 1
	fi
	
	check_common
	check_jdk
	check_db

	while read line
	do
		key=`echo $line | awk -F'=' '{print $1;}'`
		value=`echo $line | awk -F'=' '{print $2;}'`
		if [ "${value}" = "" ];then
			echo -e "please config [ \\033[1;31m $key \\033[0;39m ]."
			exit 1
		fi
		echo "export ${key}=${value}" >>${end_globe_conf}
	done <${bak_globe_conf}
	
	echo "export LC_CTYPE=en_US.UTF-8" >>${end_globe_conf}
	echo "export LANG=en_US.UTF-8" >>${end_globe_conf}
}

## 检查配置不可为空
check_common_conf

## 检查通过
LogSucc "Check pass."
