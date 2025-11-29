package com.mikorsoft.milogdb.domain;

import com.mikorsoft.milogdb.model.UserLogDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum UserLogColumn {

	ID("id", "ID", "number", UserLogDTO::getID),
	TIMESTAMP("timestamp", "Timestamp", "datetime", UserLogDTO::getTimestamp),
	QUERY_ID("queryId", "Query ID", "text", UserLogDTO::getQueryID),
	FILTERS("filters", "Filters", "list", UserLogDTO::getFilters)
	;

	private final String field;
	private final String title;
	private final String uiType;
	private final Function<UserLogDTO, Object> getter;

	public static Map<String, Object> dtoToMap(UserLogDTO log) {
		// Turn DTOs into generic, ordered maps for the view
		Map<String, Object> row = new LinkedHashMap<>();
		Arrays.stream(values()).forEach(uic -> row.put(uic.getField(), uic.getter.apply(log)));
		return row;
	}


}
