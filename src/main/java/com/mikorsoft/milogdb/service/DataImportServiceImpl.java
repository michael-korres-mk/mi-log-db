package com.mikorsoft.milogdb.service;

import com.mikorsoft.milogdb.domain.*;
import com.mikorsoft.milogdb.repository.MiLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mikorsoft.milogdb.config.Constants.BATCH_SIZE;
import static com.mikorsoft.milogdb.domain.Regexes.IP_REGEX_RAW;

@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {

	private final MiLogRepository miLogRepository;
	private final EntityManager em;
	@Transactional
	public void importFile(LogFile log, String regex) throws IOException {

		File file = new ClassPathResource(log.getFilename()).getFile();
		Pattern p = Pattern.compile(regex);

		System.out.println("filename: " + log.getFilename());

		Function<Matcher, MiLog> method = switch (log) {
			case LogFile.ACCESS -> this::createAccessLogRecord;
			case LogFile.HDFS_DATA_XCEIVER -> this::createHdfsDataXceiverLogRecord;
			case LogFile.HDFS_FS_NAMESYSTEM -> this::createHdfsFSNamesystemLogRecord;
		};

		try (var is = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line;
			List<MiLog> logs = new ArrayList<>();
			int i = 1;
			while ((line = is.readLine()) != null) {
				Matcher m = p.matcher(line);
				if (m.matches()) {
					MiLog miLog = method.apply(m);
					logs.add(miLog);
				}

				if(i % (BATCH_SIZE * 10) == 0){
					miLogRepository.saveAll(logs);
					// help memory
					em.flush();
					em.clear();
					logs = new ArrayList<>();
				}

				i++;
			}

			if (!logs.isEmpty()) {
				miLogRepository.saveAll(logs);
			}
		}

	}


	private MiLog createAccessLogRecord(Matcher m) {
		String IP = m.group(1);
		String remoteName = m.group(2);
		String userID = m.group(3);
		String t = m.group(4);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
		LocalDateTime timestamp = OffsetDateTime.parse(t, formatter).toLocalDateTime();

		String httpMethod = m.group(5);
		String resourceRequested = m.group(6);

//			HttpStatus httpStatus = HttpStatus.resolve(Integer.parseInt(m.group(6)));
		int httpStatus = Integer.parseInt(m.group(7));
		long size = Long.parseLong(m.group(8));
		String referrer = m.group(9);
		String userAgent = m.group(10);

		MiLog log = MiLog
				.builder()
				.IP(IP)
				.timestamp(timestamp)
				.size(size)
				.logType(LogType.ACCESS)
				.build();

		AccessLogDetails details = AccessLogDetails.builder()
				.remoteName(remoteName)
				.userID(userID)
				.resourceRequested(resourceRequested)
				.httpMethod(httpMethod)
				.httpStatus(httpStatus)
				.referrer(referrer)
				.userAgent(userAgent)
				.build();

		log.setAccessLogDetails(details);
		details.setMiLog(log);

		return log;
	}

	private MiLog createHdfsDataXceiverLogRecord(Matcher m) {

		String t = m.group(1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd HHmmss");
		LocalDateTime timestamp = LocalDateTime.parse(t, formatter);

		String logType = m.group(2);

		long blockID = Long.parseLong(m.group(3));

		String srcOrDestIP = m.group(4);

		String destIP = m.group(5);
		Long size = (m.group(6) == null) ? null : Long.parseLong(m.group(6));

		MiLog.MiLogBuilder miLogBuilder = MiLog
				.builder()
				.timestamp(timestamp);

		if (logType == null) {
			throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");
		} else if (logType.equals("Receiving")) {

			MiLog log = miLogBuilder
					.IP(srcOrDestIP)
					.logType(LogType.RECEIVING)
					.build();

			HdfsLogDetails details = HdfsLogDetails.builder()
					.destinationIPs(destIP)
					.blockID(blockID)
					.build();

			log.setHdfsLogDetails(details);
			details.setMiLog(log);
			return log;

		} else if (logType.equals("Received")) {
			MiLog log = miLogBuilder
					.IP(srcOrDestIP)
					.logType(LogType.RECEIVED)
					.size(size)
					.build();

			HdfsLogDetails details = HdfsLogDetails.builder()
					.destinationIPs(destIP)
					.blockID(blockID)
					.build();

			log.setHdfsLogDetails(details);
			details.setMiLog(log);
			return log;

		} else if (logType.matches(IP_REGEX_RAW)) {
			MiLog log = miLogBuilder
					.IP(logType)
					.logType(LogType.SERVED)
					.size(size)
					.build();

			HdfsLogDetails details = HdfsLogDetails.builder()
					.destinationIPs(srcOrDestIP)
					.blockID(blockID)
					.build();

			log.setHdfsLogDetails(details);
			details.setMiLog(log);
			return log;
		}

		throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");

	}

	private MiLog createHdfsFSNamesystemLogRecord(Matcher m) {

		String t = m.group(1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd HHmmss");
		LocalDateTime timestamp = LocalDateTime.parse(t, formatter);

		String replicate = m.group(3);
		String updated = m.group(6);

		if (replicate != null) {
			String sourceIP = m.group(2);
			long blockID = Long.parseLong(m.group(4));
			List<String> IPs = List.of(m.group(5).split(" "));

			MiLog log = MiLog
					.builder()
					.timestamp(timestamp)
					.logType(LogType.REPLICATE)
					.IP(sourceIP)
					.build();

			HdfsLogDetails details = HdfsLogDetails.builder()
					.blockID(blockID)
					.destinationIPs(String.join(",", IPs))
					.build();

			log.setHdfsLogDetails(details);
			details.setMiLog(log);
			return log;

		} else if (updated != null) {
			String sourceIP = m.group(7);
			long blockID = Long.parseLong(m.group(8));
			long size = Long.parseLong(m.group(9));

			MiLog log = MiLog
					.builder()
					.timestamp(timestamp)
					.logType(LogType.UPDATED)
					.IP(sourceIP)
					.size(size)
					.build();

			HdfsLogDetails details = HdfsLogDetails
					.builder()
					.blockID(blockID)
					.build();

			log.setHdfsLogDetails(details);
			details.setMiLog(log);
			return log;

		} else {
			throw new RuntimeException("Invalid HDFS FS Namesystem Log !!!");
		}

	}

}
