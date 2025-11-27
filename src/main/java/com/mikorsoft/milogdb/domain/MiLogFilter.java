package com.mikorsoft.milogdb.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum MiLogFilter {

	FROM("from", "Date From:", "date","2000-01-01"),
	TO("to", "Date To:", "date","2025-01-01"),
	LOG_TYPE("logType", "Action Type", "text",""),
	DAY("day", "Day", "date",""),
	HOUR("hour", "Hour", "number", "1"),
	SIZE("size", "Size", "number","286"),
	HTTP_METHOD("httpMethod", "HTTP Method", "text","GET"),
	HTTP_METHODS("httpMethods", "HTTP Methods", "text","GET,POST")

	;

	private final String field;
	private final String title;
	private final String uiType;
	private final String defaultValue;
	public static List<MiLogFilter> none(){
		return List.of();
	}

	public static List<MiLogFilter> timerange(){
		return List.of(FROM,TO);
	}

}
