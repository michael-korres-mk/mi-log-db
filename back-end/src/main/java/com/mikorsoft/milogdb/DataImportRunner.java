package com.mikorsoft.milogdb;

import com.mikorsoft.milogdb.service.DataImportService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataImportRunner implements ApplicationRunner {

	private final DataImportService dataImportService;

	public DataImportRunner(DataImportService dataImportService) {this.dataImportService = dataImportService;}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		dataImportService.importFile(LogFile.ACCESS, Regexes.ACCESS_LOG_REGEX);
//		dataImportService.importFile(LogFile.HDFS_DATA_XCEIVER, Regexes.HDFS_DATA_XCEIVER_LOG_REGEX);
//		dataImportService.importFile(LogFile.HDFS_FS_NAMESYSTEM, Regexes.HDFS_FS_NAMESYSTEM_LOG_REGEX);
	}
}
