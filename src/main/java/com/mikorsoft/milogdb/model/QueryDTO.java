package com.mikorsoft.milogdb.model;

import java.time.LocalDate;

public interface QueryDTO {
	Long getCount();
	String getLogType();
	LocalDate getDay();
	String getIP();
	String getBlockId();
	String getReferrer();
	String getResourceRequested();
	Long getHour();

}
