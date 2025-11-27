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
public enum MiLogColumn {
	DAY("day", "Day", "date", QueryDTO::getDay),
	HOUR("hour", "Hour", "number", QueryDTO::getHour),
	LOG_TYPE("logtype", "Log Type", "text", QueryDTO::getLogType),
	COUNT("count", "Count", "number", QueryDTO::getCount),
	IP("ip", "IP", "text", QueryDTO::getIP),
	BLOCK_ID("blockId", "Block ID", "number", QueryDTO::getBlockId),
	REFERRER("referrer", "Referrer", "text", QueryDTO::getReferrer),
	RESOURCE_REQUESTED("resourceRequested", "Resource Requested", "text", QueryDTO::getResourceRequested),
	TIMESTAMP("timestamp", "Timestamp", "datetime", QueryDTO::getTimestamp),
	SIZE("size", "Size", "number", QueryDTO::getSize),
	REMOTE_NAME("remoteName", "Remote Name", "text", QueryDTO::getRemoteName),
	USER_ID("userId", "User ID", "text", QueryDTO::getUserID),
	HTTP_METHOD("httpMethod", "HTTP Method", "text", QueryDTO::getHttpMethod),
	HTTP_STATUS("httpStatus", "HTTP Status", "number", QueryDTO::getHttpStatus),
	USER_AGENT("userAgent", "User Agent", "text", QueryDTO::getUserAgent),
	DESTINATION_IPS("destinationIPs", "Destination IP(s)", "text", QueryDTO::getDestinationIPs),
	;

	private final String field;
	private final String title;
	private final String uiType;
	private final Function<QueryDTO, Object> getter;

	public static Map<String, Object> dtoToMap(QueryDTO log, List<MiLogColumn> uiColumns) {
		// Turn DTOs into generic, ordered maps for the view
		Map<String, Object> row = new LinkedHashMap<>();
		uiColumns.forEach(uic -> row.put(uic.getField(), uic.getter.apply(log)));
		return row;
	}

	public static List<MiLogColumn> miLogColumns() {
		return List.of(IP, TIMESTAMP, SIZE, LOG_TYPE, REMOTE_NAME, USER_ID, HTTP_METHOD, HTTP_STATUS, RESOURCE_REQUESTED, REFERRER, USER_AGENT);
	}


}
