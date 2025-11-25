package com.mikorsoft.milogdb.controller;

import com.mikorsoft.milogdb.domain.LogFile;
import com.mikorsoft.milogdb.domain.Regexes;
import com.mikorsoft.milogdb.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data-import")
public class DataImportController {

	private final DataImportService dataImportService;

	@GetMapping("/{n}")
	ResponseEntity<Void> importData(@PathVariable(required = false) Long n) throws IOException {
		dataImportService.importFile(LogFile.ACCESS, Regexes.ACCESS_LOG_REGEX,n);
		dataImportService.importFile(LogFile.HDFS_DATA_XCEIVER, Regexes.HDFS_DATA_XCEIVER_LOG_REGEX,n);
		dataImportService.importFile(LogFile.HDFS_FS_NAMESYSTEM, Regexes.HDFS_FS_NAMESYSTEM_LOG_REGEX,n);
		return ResponseEntity.ok().build();
	}

}
