package com.mikorsoft.milogdb.service;

import java.io.IOException;

public interface DataImportService {

	void importFile(String filename,String regex) throws IOException;
}
