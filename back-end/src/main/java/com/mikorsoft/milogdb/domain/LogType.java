package com.mikorsoft.milogdb.domain;

public enum LogType {

	// HDFSDataXceiver
	RECEIVING,
	RECEIVED,
	SERVED,

	// HDFSFsNamesystem
	REPLICATE,
	UPDATE
}
