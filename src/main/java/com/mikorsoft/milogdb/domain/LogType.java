package com.mikorsoft.milogdb.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogType {
	// access_log
	ACCESS(Regexes.ACCESS_LOG_REGEX),

	// HDFSDataXceiver
	RECEIVING(Regexes.HDFS_DATA_XCEIVER_LOG_REGEX),
	RECEIVED(Regexes.HDFS_DATA_XCEIVER_LOG_REGEX),
	SERVED(Regexes.HDFS_DATA_XCEIVER_LOG_REGEX),

	// HDFSFsNamesystem
	REPLICATE(Regexes.HDFS_FS_NAMESYSTEM_LOG_REGEX),
	UPDATED(Regexes.HDFS_FS_NAMESYSTEM_LOG_REGEX);

	private final String regex;
}
