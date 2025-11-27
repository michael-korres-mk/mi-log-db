package com.mikorsoft.milogdb.domain;

import com.mikorsoft.milogdb.model.QueryDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum MiLogColumns {
	DAY("day", "Day", "date",QueryDTO::getDay),
	HOUR("hour", "Hour", "number",QueryDTO::getHour),
	LOG_TYPE("logtype", "Log Type", "text",QueryDTO::getLogType),
	COUNT("count", "Count", "number",QueryDTO::getCount),
	IP("ip", "IP", "text",QueryDTO::getIP),
	BLOCK_ID("blockId", "Block ID", "number",QueryDTO::getBlockId),
	REFERRER("referrer", "Referrer", "text",QueryDTO::getReferrer),
	RESOURCE_REQUESTED("resourceRequested", "Resource Requested", "text",QueryDTO::getResourceRequested),
	;

	private final String field;
	private final String title;
	private final String uiType;
	private final Function<QueryDTO,Object> getter;

	public static Map<String, Object> dtoToMap(QueryDTO log, List<MiLogColumns> uiColumns) {
		// Turn DTOs into generic, ordered maps for the view
		Map<String, Object> row = new LinkedHashMap<>();
		uiColumns.forEach(uic -> row.put(uic.getField(),uic.getter.apply(log)));
		return row;
	}


}
