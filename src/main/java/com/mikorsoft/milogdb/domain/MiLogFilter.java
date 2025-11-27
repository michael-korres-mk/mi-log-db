package com.mikorsoft.milogdb.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum MiLogFilter {

	FROM("from", "Date From:", "date"),
	TO("to", "Date To:", "date"),
	LOG_TYPE("logtype", "Action Type", "text"),
	DAY("day", "Day", "date"),
	SIZE("size", "Size", "number"),
	HTTP_METHOD("httpMethod", "HTTP Method", "text"),
	HTTP_METHODS("httpMethods", "HTTP Methods", "text"),
	;

	private final String field;
	private final String title;
	private final String uiType;

	public static List<MiLogFilter> none(){
		return List.of();
	}

	public static List<MiLogFilter> timerange(){
		return List.of(FROM,TO);
	}

}
