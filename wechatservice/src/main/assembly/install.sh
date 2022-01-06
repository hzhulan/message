#!/bin/bash

#history:
#2021-09-24 消息中心安装

## 取当前绝对路径
workDir=`dirname $0`
cd $workDir >/dev/null
workDir=`pwd`

dos2unix -q ./log.sh
. ./log.sh

dos2unix -q ./startup.sh


dos2unix -q ./globe.common.conf
source ./globe.common.conf

# 取今天的年月日
today=`date +%Y%m%d`

INSTALLPATH=`pwd`

## 模块名
module_name=${MODULE_NAME}

## 安装日志
readonly logFile="$INSTALLPATH/install_${module_name}_$today.log"
SetLogFile $logFile

## 安装路径
MESSAGE_PATH="/home/install/${module_name}"
if [ -z "$2" ]
then
    LogInfo "use default install path: " ${MESSAGE_PATH}
else
    MESSAGE_PATH=$2
    `mkdir -p ${MESSAGE_PATH}`
    LogInfo "use custom install path: " ${MESSAGE_PATH}
fi


APP_PATH=$MESSAGE_PATH/$module_name

## 默认版本不一致
EQUAL_VER=1

sh ./checkenv.sh
if [ $? -ne 0 ];then
	exit 1
fi

#执行命令并写入日志中
function doCommand()
{
	LogInfo "$*"
	$*
}

function showHelp()
{
	echo -e "\nUsage: "
	echo -e "    sh $0 install"
	echo -e "    sh $0 rollback"
	echo -e "    sh $0 reinstall"
	echo -e "    sh $0 uninstall"
	echo -e "\nOPTIONS:"
	echo -e "    --help"
	exit 1
}

function install_module()
{
	  LogInfo "begin to install ${module_name} ..."

    ## 版本一致则不装
    if [ $EQUAL_VER -eq 0 ];then
        LogWarning "the setup version is $COMPONENT_VERSION, and equal the currently version! not need to install."
        exit 1
    fi

    ## 备份系统
    backsys

    ## 替换配置文件
    source ./load_config.sh

	  ## 安装模块文件
	  rm -rf $MESSAGE_PATH/${module_name}/
	  mkdir -p $MESSAGE_PATH/${module_name}/log

	  cd $workDir
	  cp -rf bin/ $MESSAGE_PATH/${module_name}/
	  cp -rf conf/ $MESSAGE_PATH/${module_name}/
	  cp -rf lib/ $MESSAGE_PATH/${module_name}/
	  cp release-note $MESSAGE_PATH/${module_name}/
	  cp startup.sh $MESSAGE_PATH/${module_name}/
	  cp globe.common.conf $MESSAGE_PATH/${module_name}/

	  dos2unix -q $MESSAGE_PATH/${module_name}/*.sh
	  chmod 755 $MESSAGE_PATH/${module_name}/*.sh

	  config_module

}

function config_module()
{
	LogInfo "begin to config ${module_name} ..."
	cd $workDir

	## 格式化配置文件
	dos2unix -q $MESSAGE_PATH/${module_name}/conf/*.*
	chmod 644 $MESSAGE_PATH/${module_name}/conf/*.*


  sh startup.sh

  LogInfo "等待服务启动"
  sleep 15

	checkRunning

	if [ $? -ne 0 ]; then
	    ensureRollback
  fi

}

function ensureRollback() {
    read -p "是否需要回滚（1需要 0不需要）: " number  #提示用户输入数字
    if [ $number -eq 1 ];then          #判断计算结果是否为0，为0则说明number非数字(字符串和0相乘结果为0)
        LogInfo "---------------- execute rollback ----------------"
        rollback
    else
        LogInfo "取消回滚。如有回滚需要，请手动sh install.sh rollback"
    fi
}

function rollback() {

    cd $MESSAGE_PATH

    lastVersionDir=`ls|grep {module_name}_bak|tail -1`

    if [ ! -n "$lastVersionDir" ];then
        LogError "没有备份的文件服务回滚"
        exit 1;
    fi

    LogInfo "模块回滚"
    rm -rf ${module_name}
    cp -rf $lastVersionDir ${module_name}

   sh startup.sh

    sleep 15

    checkRunning


    if [ $? -ne 0 ]; then
        LogError "rollback error."
        return 1;
    else
        LogSucc "rollback ${module_name} successfully."
    fi

    cd $workDir
}


function checkRunning() {

    service=`lsof -i:${SERVER_PORT}|grep -v "PID" | awk '{print $2}'`

    if [ ! -n "${service}"  ];then
        LogError "端口${SERVER_PORT}未正常启动!"
        return 1
    fi

    LogSucc "install ${module_name} successfully."

}


## 获取版本，并判断是否一致
function getVersion()
{
	## 获取安装包版本
	if [ -f ${INSTALLPATH}/release-note ];then
		COMPONENT_VERSION=`grep "^Version=" ${INSTALLPATH}/release-note | tail -1 | cut -d'=' -f2`
		LogInfo "SCRIPT VERION: $COMPONENT_VERSION"
	else
		LogError "${INSTALLPATH}/release-note does not exist."
		exit 1
	fi

	## 获取已安装版本
	if [ -f ${APP_PATH}/release-note ];then
		sys_ver=`grep "^Version=" ${APP_PATH}/release-note | tail -1 | cut -d'=' -f2`
		LogInfo "${MODULENAME} VERION: $sys_ver"
	else
		LogInfo "${APP_PATH}/release-note does not exist."
		sys_ver="-1"
	fi

	## 判断版本一致性
	if [ "x${COMPONENT_VERSION}" = "x${sys_ver}" ];then
		EQUAL_VER=0
	fi
}

function backsys()
{
	LogInfo "begin to back sysfile ..."
	if [ ! -d $APP_PATH -o ! -f $APP_PATH/release-note ];then
		LogInfo "$APP_PATH does not exist,can not back system."
		return
	fi

	bakdir="${module_name}_bak_${sys_ver}_${today}"
	rm -rf $bakdir

	cd $MESSAGE_PATH
	cp -rf $module_name $bakdir

	## 清理日志
	find $bakdir -name "log" | xargs rm -rf

	## 清理备份，最多保留3份
	bakcnt=`ls | grep "${module_name}_bak_*" | wc -l`
	if [ $bakcnt -gt 3 ];then
		let rmcnt=$bakcnt-3
		for fdir in `ls | grep "${module_name}_bak_*" | head -$rmcnt`
		do
			LogInfo "remove bak system dir : $fdir"
			rm -rf $fdir
		done
	fi

	cd $workDir
}

function uninstall_module()
{
	LogInfo "begin to uninstall ${module_name} ..."
	rm -rf $MESSAGE_PATH/${module_name}/

	LogSucc "uninstall ${module_name} successfully."
}

case "$1" in
		--help)
			showHelp
			;;
		install)
		    getVersion
			install_module
			;;
		reinstall)
			uninstall_module
			install_module
			;;
		uninstall)
			uninstall_module
			;;
        rollback)
            rollback
            ;;
		*)
			LogError "please input the parameter..."
			showHelp
			;;
esac

exit 0

