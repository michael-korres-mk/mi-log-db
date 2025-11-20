package com.mikorsoft.milogdb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
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

//	143.127.131.4 - - [09/Jun/2005:23:48:54 -0400] "GET / HTTP/1.1" 403 3931 "-" "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98)"

	private final String IP_REGEX = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
	private final String TIMESTAMP_REGEX = "\\[(\\d{2}/[A-Za-z]{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} -\\d{4})]";

	private final String ANY_CHARACTER = ".*";

	private final String accessLogRegex = "^" + IP_REGEX + " - - " + TIMESTAMP_REGEX +


//				"\"([^\"]*)\"" + " (\\d{3}) " + "(\\d+|-) " +
//				"\"([^\"]*)\" \"([^\"]*)\"$" +
			ANY_CHARACTER
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

			String timestamp = m.group(2);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
			ZonedDateTime zdt = ZonedDateTime.parse(timestamp, formatter);
			System.out.println("timestamp = " + zdt);



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
