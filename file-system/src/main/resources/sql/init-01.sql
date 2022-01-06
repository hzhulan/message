drop table if exists f_album;
CREATE TABLE f_album (
  `id` int NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '相册名称',
  `sort` int NOT NULL COMMENT '排序',
  `status` varchar(50) NOT NULL COMMENT '相册状态0删除1正常',
  PRIMARY KEY (`id`)
);

drop table if exists f_dict;
CREATE TABLE `f_dict` (
    `id` int NOT NULL AUTO_INCREMENT,
    `p_type` varchar(50) NOT NULL COMMENT '字典父类型',
    `dict_type` varchar(50) NOT NULL COMMENT '字典类型',
    `dict_code` varchar(50) NOT NULL COMMENT '字典code',
    `dict_value` varchar(100) NOT NULL COMMENT '字典value',
    PRIMARY KEY (`id`)
);


drop table if exists f_file;
CREATE TABLE `f_file` (
    `id` int NOT NULL AUTO_INCREMENT,
    `url` varchar(150) NOT NULL COMMENT '图片url',
    `file_name` varchar(150) NOT NULL COMMENT '图片url',
    `create_time` bigint NOT NULL default 0 COMMENT '图片时间',
    `insert_time` bigint NOT NULL default 0 COMMENT '插入时间',
    `album_id` int NOT NULL DEFAULT '1' COMMENT '相册id(文件夹)',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态1正常0删除',
    PRIMARY KEY (`id`)
);
