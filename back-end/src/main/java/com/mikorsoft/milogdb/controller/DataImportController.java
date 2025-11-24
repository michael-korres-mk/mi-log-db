package com.mikorsoft.milogdb.controller;

import com.mikorsoft.milogdb.domain.LogFile;
import com.mikorsoft.milogdb.domain.Regexes;
import com.mikorsoft.milogdb.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data-import")
public class DataImportController {

	private final DataImportService dataImportService;

	@GetMapping
	ResponseEntity<Void> importData() throws IOException {
		dataImportService.importFile(LogFile.ACCESS, Regexes.ACCESS_LOG_REGEX);
		dataImportService.importFile(LogFile.HDFS_DATA_XCEIVER, Regexes.HDFS_DATA_XCEIVER_LOG_REGEX);
		dataImportService.importFile(LogFile.HDFS_FS_NAMESYSTEM, Regexes.HDFS_FS_NAMESYSTEM_LOG_REGEX);
		return ResponseEntity.ok().build();
	}

}
