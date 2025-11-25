package com.mikorsoft.milogdb.domain;

import java.util.List;

public class Regexes {

	private static final String START_ANCHOR = "^";
	public static final String IP_REGEX_RAW = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(?::\\d+)?";
	private static final String IP_REGEX = "(" + IP_REGEX_RAW + ")";
	private static final String IPS_REGEX = "((?:" + IP_REGEX_RAW + "\\s*)+)";
	private static final String NAME_REGEX = "([A-Za-z0-9_.]+|-)";
	private static final String REMOTE_NAME_REGEX = NAME_REGEX;
	private static final String USER_ID_REGEX = NAME_REGEX;
	private static final String ACCESS_LOG_TIMESTAMP_REGEX = "\\[(\\d{2}/[A-Za-z]{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} -\\d{4})]";
	private static final String HTTP_METHOD_AND_RESOURCE_REGEX = "\"([A-Z]{3,}) (/[A-Za-z0-9_]*) HTTP/\\d.\\d\"";
	private static final String HTTP_RESPONSE_STATUS_REGEX = "(\\d{3})";
	private static final String INTEGER_NUM_REGEX = "(-?\\d+)";
	private static final String RESPONSE_SIZE_REGEX = INTEGER_NUM_REGEX;
	private static final String REFERRER_REGEX = "\"" + NAME_REGEX + "\"";
	private static final String USER_AGENT_REGEX = "\"([^\"]*)\""; // get anything between A PAIR OF quotes (!= .*)
	private static final String ANY_CHARACTER = ".*";
	private static final String END_ANCHOR = "$";

	public static final String ACCESS_LOG_REGEX =
			START_ANCHOR +
					String.join(" ", List.of(
							IP_REGEX,
							REMOTE_NAME_REGEX,
							USER_ID_REGEX,
							ACCESS_LOG_TIMESTAMP_REGEX,
							HTTP_METHOD_AND_RESOURCE_REGEX,
							HTTP_RESPONSE_STATUS_REGEX,
							RESPONSE_SIZE_REGEX,
							REFERRER_REGEX,
							USER_AGENT_REGEX
					)) +
					END_ANCHOR
			;

	private static final String HDFS_TIMESTAMP_REGEX = "(\\d{6} \\d{6}) \\d{2,3}";
	private static final String BLOCK_ID_REGEX = "blk_" + INTEGER_NUM_REGEX;
	private static final String SOURCE_IP_REGEX = IP_REGEX;
	private static final String DESTINATION_IPS_REGEX = IPS_REGEX;
	private static final String SIZE_REGEX = INTEGER_NUM_REGEX;
	private static final String HDFS_FS_NAMESYSTEM_TYPE_REGEX = "(replicate|updated)";

	private static final String HDFS_FS_NAMESYSTEM_REPLICATE_LOG_REGEX = "ask " + SOURCE_IP_REGEX + " to " + HDFS_FS_NAMESYSTEM_TYPE_REGEX + " " + BLOCK_ID_REGEX + " to datanode\\(s\\) " + DESTINATION_IPS_REGEX;
	private static final String HDFS_FS_NAMESYSTEM_UPDATE_LOG_REGEX = "NameSystem\\.addStoredBlock: blockMap " + HDFS_FS_NAMESYSTEM_TYPE_REGEX + ": " + SOURCE_IP_REGEX + " is added to "  + BLOCK_ID_REGEX + " size " + SIZE_REGEX;
	public static final String HDFS_FS_NAMESYSTEM_LOG_REGEX =
			START_ANCHOR +
					String.join(" ", List.of(
							HDFS_TIMESTAMP_REGEX,
							"[A-Z]+",
							"dfs.FSNamesystem:",
							"BLOCK\\*",
							"(?:" + HDFS_FS_NAMESYSTEM_REPLICATE_LOG_REGEX + "|" + HDFS_FS_NAMESYSTEM_UPDATE_LOG_REGEX + ")"
					)) +
					END_ANCHOR
			;

	public static final String HDFS_DATA_XCEIVER_LOG_REGEX =
			START_ANCHOR +
					String.join(" ", List.of(
							HDFS_TIMESTAMP_REGEX,
							"[A-Z]+",
							"dfs\\.DataNode\\$DataXceiver:",
							"(Receiving|Received|" + IP_REGEX_RAW + ") (?:Served )?block",
							BLOCK_ID_REGEX,
							"(?:src:|to) /" + IP_REGEX,
							"(?:dest: /" + IP_REGEX + ")?"
							,"(?:of size " + SIZE_REGEX + ")?"

					)) +
					END_ANCHOR
			;


}
