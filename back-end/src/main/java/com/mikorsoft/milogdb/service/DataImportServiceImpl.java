package com.mikorsoft.milogdb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {

	private final String START_ANCHOR = "$";
	private final String IP_REGEX = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
	private final String NAME_REGEX = "([A-Za-z0-9_.]+|-)";
	private final String REMOTE_NAME_REGEX = NAME_REGEX;
	private final String USER_ID_REGEX = NAME_REGEX;
	private final String TIMESTAMP_REGEX = "\\[(\\d{2}/[A-Za-z]{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} -\\d{4})]";
	private final String HTTP_METHOD_REGEX = "\"([A-Z]{3,}) / HTTP/\\d.\\d\"";
	private final String HTTP_RESPONSE_STATUS_REGEX = "(\\d{3})";
	private final String RESPONSE_SIZE_REGEX = "(\\d+)";
	private final String REFERRER_REGEX = "\"" + NAME_REGEX + "\"";
	private final String USER_AGENT_REGEX = "\"([^\"]*)\""; // get anything between A PAIR OF quotes (!= .*)

	private final String ANY_CHARACTER = ".*";
	private final String END_ANCHOR = "$";

	private final String accessLogRegex =
			START_ANCHOR +
				IP_REGEX + " " +
				REMOTE_NAME_REGEX + " " +
				USER_ID_REGEX + " " +
				TIMESTAMP_REGEX + " " +
				HTTP_METHOD_REGEX + " " +
				HTTP_RESPONSE_STATUS_REGEX + " " +
				RESPONSE_SIZE_REGEX + " " +
				REFERRER_REGEX + " " +
				USER_AGENT_REGEX +
			END_ANCHOR
			;

	public void importFile(String filename,String regex) throws IOException {

		System.out.println("IMPORT FILE !");


		File file = new ClassPathResource(filename).getFile();
		Pattern p = Pattern.compile(accessLogRegex);

		try(var is = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
			String line;
			int n = 10;
			while ((n-- > 0) && !(line = is.readLine()).isEmpty()) {
				Matcher m = p.matcher(line);

				this.createAccessLogRecord(m);

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



//					String threadId = m.group(3);
//					System.out.println("threadId = " + threadId);
//
//					String level = m.group(4);
//					System.out.println("level = " + level);
//
//					String component  = m.group(5);
//					System.out.println("component = " + component);
//
//					String blockId = m.group(6);
//					System.out.println("blockId = " + blockId);
//
//					String src  = m.group(7);
//					System.out.println("src = " + src);
//
//					String dest = m.group(8);
//					System.out.println("dest = " + dest);

//					new AccessLogRecord(date,time,threadId,level,component,blockId,src,dest);

		}
	}

}
