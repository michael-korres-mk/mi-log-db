package com.mikorsoft.milogdb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface QueryDTO {
	Long getID();
	Long getCount();
	String getLogType();
	LocalDate getDay();
	String getIP();
	String getBlockId();
	String getReferrer();
	String getResourceRequested();
	Long getHour();

	LocalDateTime getTimestamp();
	Long getSize();
	String getRemoteName();
	String getUserID();
	String getHttpMethod();
	Integer getHttpStatus();
	String getUserAgent();
	String getDestinationIPs();

}
