package com.mikorsoft.milogdb.service;

import org.springframework.stereotype.Service;

@Service
public class DataImportService {

//	143.127.131.4 - - [09/Jun/2005:23:48:54 -0400] "GET / HTTP/1.1" 403 3931 "-" "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98)"

	String accessLogRegex = "^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) - - " +
			"\\[(\\d{1,2}/[A-Za-z]{3}/\\d{4}:\\d{1,2}:\\d{1,2}:\\d{1,2} -\\d{4})]" +
			"\"(\\w+) ([^ ]+) (HTTP/[0-9.]+)\" (\\d{3}) (\\d+) " +
	        "\"([^\"]*)\" \"([^\"]*)\"$";

	;


}
