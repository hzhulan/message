#业务配置，举例如下：
applicationConfig=./conf/application.properties


## *************************** Step 1: 环境配置 ***************************


## *************************** Step 2: 消息中心配置 ***************************
sed -i "s#active.*#active=${ENV}#g" $applicationConfig
