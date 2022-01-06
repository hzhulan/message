#!/bin/bash

SETCOLOR_SUCCESS="echo -en \\033[1;32m";
SETCOLOR_FAILURE="echo -en \\033[1;31m";
SETCOLOR_WARNING="echo -en \\033[1;33m";
SETCOLOR_NORMAL="echo -en \\033[0;39m";
export LANG=en_US.UTF-8
export LC_CTYPE=en_US.UTF-8

FCLOG_FILENAME=log
FCLOG_ISLOGFILE=0


function SetLogFile()
{
	if [ $# == 1 ];then
		FCLOG_ISLOGFILE=1;
		FCLOG_FILENAME=$1;
		return 0;
	else
		return 1;
	fi;
}


function LogInfo()
{
	time=`date "+%m-%d %T"`;
	log="$time [INFO] $*";
	if [ $FCLOG_ISLOGFILE != 0 ];then
		echo -e $log >> $FCLOG_FILENAME;
	fi;
	echo -e $log;
}

function LogError()
{
	$SETCOLOR_FAILURE;
	time=`date "+%m-%d %T"`;
	log="$time [ERROR] $*";
	if [ $FCLOG_ISLOGFILE != 0 ];then
		echo -e $log >> $FCLOG_FILENAME;
	fi;
	echo -e $log;
	$SETCOLOR_NORMAL;
}

function LogWarning()
{
	$SETCOLOR_WARNING
	time=`date "+%m-%d %T"`;
	log="$time [WARN] $*";
	if [ $FCLOG_ISLOGFILE != 0 ];then
		echo -e $log >> $FCLOG_FILENAME;
	fi;
	echo -e $log;
	$SETCOLOR_NORMAL
}

function LogDebug()
{
	time=`date "+%m-%d %T"`;
	log="$time [DEBUG] $*";
	if [ $FCLOG_ISLOGFILE != 0 ];then
		echo -e $log >> $FCLOG_FILENAME;
	fi;
}

function LogSucc()
{
	$SETCOLOR_SUCCESS
	time=`date "+%m-%d %T"`;
	log="$time [SUCC] $*";
	if [ $FCLOG_ISLOGFILE != 0 ];then
		echo -e $log >> $FCLOG_FILENAME;
	fi;
	echo -e $log;
	$SETCOLOR_NORMAL
}
