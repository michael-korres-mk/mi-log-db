package com.mikorsoft.milogdb.service;

import com.mikorsoft.milogdb.domain.LogFile;
import com.mikorsoft.milogdb.domain.LogType;
import com.mikorsoft.milogdb.domain.MiLog;
import com.mikorsoft.milogdb.repository.MiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mikorsoft.milogdb.domain.Regexes.IP_REGEX_RAW;

@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {

	private final MiLogRepository miLogRepository;

	public void importFile(LogFile log, String regex) throws IOException {

		File file = new ClassPathResource(log.getFilename()).getFile();
		Pattern p = Pattern.compile(regex);

		System.out.println("filename: " + log.getFilename());

		Function<Matcher, MiLog> method = switch (log) {
			case LogFile.ACCESS -> this::createAccessLogRecord;
			case LogFile.HDFS_DATA_XCEIVER -> this::createHdfsDataXceiverLogRecord;
			case LogFile.HDFS_FS_NAMESYSTEM -> this::createHdfsFSNamesystemLogRecord;
		};

		try(var is = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
			String line;
			// TODO: Remove limit
			int n = 50;
			List<MiLog> logs = new ArrayList<>();
			while ((n > 0) && (line = is.readLine()) != null) {
				Matcher m = p.matcher(line);
				if (m.matches()) {
					MiLog miLog = method.apply(m);
					System.out.println(miLog);
					logs.add(miLog);
					n--;
				}
			}
			miLogRepository.saveAll(logs);
		}

	}


	private MiLog createAccessLogRecord(Matcher m) {
		String IP = m.group(1);
		String remoteName = m.group(2);
		String userID = m.group(3);
		String t = m.group(4);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);ZonedDateTime timestamp = ZonedDateTime.parse(t, formatter);
		String httpMethod = m.group(5);

//			HttpStatus httpStatus = HttpStatus.resolve(Integer.parseInt(m.group(6)));
		int httpStatus = Integer.parseInt(m.group(6));
		long size = Long.parseLong(m.group(7));
		String referrer = m.group(8);
		String userAgent = m.group(9);

		return MiLog
				.builder()
				.IP(IP)
				.timestamp(timestamp)
				.size(size)
				.logType(LogType.ACCESS)
				.remoteName(remoteName)
				.userID(userID)
				.httpMethod(httpMethod)
				.httpStatus(httpStatus)
				.referrer(referrer)
				.userAgent(userAgent)
				.build();

	}
	private MiLog createHdfsDataXceiverLogRecord(Matcher m){

		String t = m.group(1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd HHmmss");
		ZonedDateTime timestamp = LocalDateTime.parse(t, formatter).atZone(ZoneId.of("UTC"));

		String logType = m.group(2);

		long blockID = Long.parseLong(m.group(3));

		String srcOrDestIP = m.group(4);

		String destIP = m.group(5);
		Long size = (m.group(6) == null)? null : Long.parseLong(m.group(6));

		MiLog.MiLogBuilder miLogBuilder = MiLog
				.builder()
				.timestamp(timestamp)
				.blockID(blockID);

		if(logType == null){
			throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");
		} else if (logType.equals("Receiving")) {

			return miLogBuilder
					.IP(srcOrDestIP)
					.logType(LogType.RECEIVING)
					.destinationIPs(destIP)
					.build();

		}
		else if (logType.equals("Received")) {
			return miLogBuilder
					.IP(srcOrDestIP)
					.logType(LogType.RECEIVED)
					.destinationIPs(destIP)
					.size(size)
					.build();
		}
		else if(logType.matches(IP_REGEX_RAW)){
			return miLogBuilder
					.IP(logType)
					.logType(LogType.SERVED)
					.destinationIPs(srcOrDestIP)
					.size(size)
					.build();
		}

		throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");

	}

	private MiLog createHdfsFSNamesystemLogRecord(Matcher m){


			String t = m.group(1);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd HHmmss");
			ZonedDateTime timestamp = LocalDateTime.parse(t, formatter).atZone(ZoneId.of("UTC"));
			System.out.println("timestamp = " + timestamp);

			String replicate = m.group(3);
			String updated = m.group(6);

			if(replicate != null){
				String sourceIP = m.group(2);
				long blockID = Long.parseLong(m.group(4));
				List<String> IPs = List.of(m.group(5).split(" "));

				return MiLog
						.builder()
						.timestamp(timestamp)
						.logType(LogType.REPLICATE)
						.IP(sourceIP)
						.blockID(blockID)
						.destinationIPs(String.join(",",IPs))
						.build();

			} else if (updated != null) {
				String sourceIP = m.group(7);
				long blockID = Long.parseLong(m.group(8));
				long size = Long.parseLong(m.group(9));

				return MiLog
						.builder()
						.timestamp(timestamp)
						.logType(LogType.UPDATED)
						.IP(sourceIP)
						.blockID(blockID)
						.size(size)
						.build();

			}
			else {
				throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");
			}

	}

}
