package com.mikorsoft.milogdb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {
	private static final String START_ANCHOR = "^";
	private static final String IP_REGEX_RAW = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(?::\\d+)?";
	private static final String IP_REGEX = "(" + IP_REGEX_RAW + ")";
	private static final String IPS_REGEX = "((?:" + IP_REGEX_RAW + "\\s*)+)";
	private static final String NAME_REGEX = "([A-Za-z0-9_.]+|-)";
	private static final String REMOTE_NAME_REGEX = NAME_REGEX;
	private static final String USER_ID_REGEX = NAME_REGEX;
	private static final String ACCESS_LOG_TIMESTAMP_REGEX = "\\[(\\d{2}/[A-Za-z]{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} -\\d{4})]";
	private static final String HTTP_METHOD_REGEX = "\"([A-Z]{3,}) / HTTP/\\d.\\d\"";
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
				HTTP_METHOD_REGEX,
				HTTP_RESPONSE_STATUS_REGEX,
				RESPONSE_SIZE_REGEX,
				REFERRER_REGEX,
				USER_AGENT_REGEX
			)) +
			END_ANCHOR
			;


//• Time stamp,
//• Block ID(s),
//• Source IP,
//• Destination IP(s),
//• Type (replicate, update — ignore other records),
//• (Optional) Size.

//	081109 203550 32 INFO dfs.FSNamesystem: BLOCK* NameSystem.allocateBlock: /user/root/rand/_temporary/_task_200811092030_0001_m_000240_0/part-00240. blk_-1812791921266891724
//  081109 203521 19 INFO dfs.FSNamesystem: BLOCK* ask 10.250.14.224:50010 to replicate blk_-1608999687919862906 to datanode(s) 10.251.215.16:50010 10.251.71.193:50010
//	                                        BLOCK* ask 10.250.1.1:50010 to replicate blk_-123 to datanode(s)10.0.0.1 10.0.0.2
	private static final String HDFS_TIMESTAMP_REGEX = "(\\d{6} \\d{6}) \\d{2,3}";
	private static final String BLOCK_ID_REGEX = "blk_" + INTEGER_NUM_REGEX;
	private static final String SOURCE_IP_REGEX = IP_REGEX;
	private static final String DESTINATION_IPS_REGEX = IPS_REGEX;
	private static final String SIZE_REGEX = INTEGER_NUM_REGEX;
	private static final String HDFS_FS_NAMESYSTEM_TYPE_REGEX = "(replicate|updated)";

//	Type (replicate, update — ignore other records),

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


//	081109 203518 143 INFO dfs.DataNode$DataXceiver: Receiving block blk_-1608999687919862906 src: /10.250.19.102:54106 dest: /10.250.19.102:50010
//  081109 203521 143 INFO dfs.DataNode$DataXceiver: Received block blk_-1608999687919862906 src: /10.251.215.16:52002 dest: /10.251.215.16:50010 of size 91178
//  081109 203523 148 INFO dfs.DataNode$DataXceiver: 10.250.11.100:50010 Served block blk_-3544583377289625738 to /10.250.19.102


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
					ANY_CHARACTER
			;




	public void importFile(String filename,String regex) throws IOException {

		System.out.println("IMPORT FILE !");

		File file = new ClassPathResource(filename).getFile();
		Pattern p = Pattern.compile(regex);

		System.out.println("filename: " + filename);

		try(var is = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
			String line;
			int n = 50;
			while ((n-- > 0) && !(line = is.readLine()).isEmpty()) {
				Matcher m = p.matcher(line);

				this.createHdfsDataXceiverLogRecord(m);

			}
		}

	}


	private void createAccessLogRecord(Matcher m) {
		if (m.matches()) {
			String IP = m.group(1);
			System.out.println("IP = " + IP);

			String remoteName = m.group(2);
			System.out.println("remoteName = " + remoteName);

			String userID = m.group(3);
			System.out.println("userID = " + userID);

			String timestamp = m.group(4);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
			ZonedDateTime zdt = ZonedDateTime.parse(timestamp, formatter);
			System.out.println("timestamp = " + zdt);

			String httpMethod = m.group(5);
			System.out.println("httpMethod = " + httpMethod);

			HttpStatus httpResponseStatus = HttpStatus.resolve(Integer.parseInt(m.group(6)));
			System.out.println("httpResponseStatus = " + httpResponseStatus);

			long responseSize = Long.parseLong(m.group(7));
			System.out.println("responseSize = " + responseSize);

			String referrer = m.group(8);
			System.out.println("referrer = " + referrer);

			String userAgent = m.group(9);
			System.out.println("userAgent = " + userAgent);

			//					new AccessLogRecord(date,time,threadId,level,component,blockId,src,dest);

		}
	}

// • Time stamp,
// • Block ID,
// • Source IP,
// • Destination IP,
// • (Optional) Size,
// • Type (receiving, received, served — ignore other records).
	private void createHdfsDataXceiverLogRecord(Matcher m){

		if (m.matches()) {

			String timestamp = m.group(1);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd HHmmss");
			ZonedDateTime zdt = LocalDateTime.parse(timestamp, formatter).atZone(ZoneId.of("UTC"));
			System.out.println("timestamp = " + zdt);

			String logType = m.group(2);
			System.out.println("logType = " + logType);

			long blockID = Long.parseLong(m.group(3));
			System.out.println("blockID = " + blockID);

			String srcOrDestIP = m.group(4);

			String destIP = m.group(5);
			Long size = (m.group(6) == null)? null : Long.parseLong(m.group(6));

			if(logType == null){
				throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");
			} else if (logType.equals("Receiving")) {
				System.out.println(logType);
				System.out.println("source IP: " + srcOrDestIP);
				System.out.println("dest IP: " + destIP);
			}
			else if (logType.equals("Received")) {
				System.out.println(logType);
				System.out.println("source IP: " + srcOrDestIP);
				System.out.println("dest IP: " + destIP);
				System.out.println("size = " + size);
			}
			else if(logType.matches(IP_REGEX_RAW)){
				System.out.println("IP: " + logType);
				System.out.println("destination IP: " + srcOrDestIP);

			}



			System.out.println("blockID = " + blockID);


			System.out.println("----------------");

		}

	}


	private void createHdfsFSNamesystemLogRecord(Matcher m){
		
		if (m.matches()) {

			String timestamp = m.group(1);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd HHmmss");
			ZonedDateTime zdt = LocalDateTime.parse(timestamp, formatter).atZone(ZoneId.of("UTC"));
			System.out.println("timestamp = " + zdt);

			String replicate = m.group(3);
			String updated = m.group(6);

			if(replicate != null){
					System.out.println(replicate);
				String sourceIP = m.group(2);
					System.out.println("sourceIP = " + sourceIP);
				// TODO: Check if there are records with multiple blockIDs
				long blockID = Long.parseLong(m.group(4));
					System.out.println("blockID = " + blockID);
					System.out.println("Destination IPs = [");
				List<String> IPs = List.of(m.group(5).split(" "));
				IPs.forEach(System.out::println);
					System.out.println("]");

			} else if (updated != null) {
				System.out.println(updated);
				String sourceIP = m.group(7);
				System.out.println("sourceIP = " + sourceIP);
				long blockID = Long.parseLong(m.group(8));
				System.out.println("blockID = " + blockID);
				long size = Long.parseLong(m.group(9));
				System.out.println("size = " + size);
			}
			else {
				throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");
			}

//					new AccessLogRecord(date,time,threadId,level,component,blockId,src,dest);

			System.out.println("----------------");

		}

	}

}
