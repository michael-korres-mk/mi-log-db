package com.mikorsoft.milogdb;

import com.mikorsoft.milogdb.service.DataImportService;
import com.mikorsoft.milogdb.service.DataImportServiceImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataImportRunner implements ApplicationRunner {

	private final DataImportService dataImportService;

	public DataImportRunner(DataImportService dataImportService) {this.dataImportService = dataImportService;}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		dataImportService.importFile("access_log_full", DataImportServiceImpl.ACCESS_LOG_REGEX);
//		dataImportService.importFile("HDFS_DataXceiver.log", DataImportServiceImpl.HDFS_DATAXCEIVER_LOG_REGEX);

		dataImportService.importFile("HDFS_FS_Namesystem.log", DataImportServiceImpl.HDFS_FS_NAMESYSTEM_LOG_REGEX);
	}
}
