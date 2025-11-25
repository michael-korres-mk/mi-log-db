package com.mikorsoft.milogdb.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogFile {
	ACCESS("access_log_full"),
	HDFS_DATA_XCEIVER("HDFS_DataXceiver.log"),
	HDFS_FS_NAMESYSTEM("HDFS_FS_Namesystem.log")
	;

	private final String filename;

}
