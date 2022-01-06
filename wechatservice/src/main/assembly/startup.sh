#!/bin/sh

## 取当前绝对路径
binDir=`dirname $0`
cd $binDir
## --bin
binDir=`pwd`

dos2unix -q ./globe.common.conf
. ./globe.common.conf

module_name=${MODULE_NAME}

## main 方法所在类
MAIN_CLASS=com.hzhu.wechat.SpringbootApplication

JVM_OPTION="-Xms512m -Xmx512m"

export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8

## 导入系统变量
source ./globe.common.conf

## 增加运行日志
logFile=${binDir}/.run.log
errorLog=${binDir}/error.log
date "+%Y-%m-%d %H:%M:%S" >>${logFile}
echo "===============================" >>${logFile}

function loginfo()
{
	echo "$1"
	echo "$1" >>${logFile}
}

  function checkJar()
{
	cd $binDir/bin
	if [ `ls ${module_name}-*.jar | wc -l` -ne 1 ];then
		loginfo "$binDir/${module_name}-.jar not equal 1,please check."
		exit 1
	fi

	jar_name=`ls ${module_name}-*.jar`
	echo ${jar_name}

	cd $binDir
}

function checkRunning()
{
	flag=0
	for pid in `ps -elf | grep "${module_name}-.*.jar" | grep -v grep | awk '{print $4;}'`
	do
		 loginfo "${module_name} is running,pid is $pid ..."
		 flag=1
	done

	if [ $flag -eq 1 ];then
		exit 0
	fi
}

function startRun()
{
	## 检查当前是否已有进程运行
	checkRunning

	## 检查jar包是否存在且有且只有一个
	checkJar

	## 公共配置 私有配置
	confpath="./conf"

	## 主程序
	libpath="$confpath:bin/${jar_name}"

	## 公共库
	for jar in `find ./lib/ -name "*.jar" 2>/dev/null`
	do
		libpath="$libpath:$jar"
	done

	## 开始运行
	loginfo "$JDK_ROOT/bin/java -classpath $libpath $MAIN_CLASS"
	$JDK_ROOT/bin/java $JVM_OPTION -classpath $libpath $MAIN_CLASS $* >${errorLog} 2>&1 &
}

startRun $*
