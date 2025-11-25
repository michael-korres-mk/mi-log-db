package com.mikorsoft.milogdb.service;

import com.mikorsoft.milogdb.domain.LogFile;

import java.io.IOException;

public interface DataImportService {
	void importFile(LogFile log, String regex,Long n) throws IOException;
}
