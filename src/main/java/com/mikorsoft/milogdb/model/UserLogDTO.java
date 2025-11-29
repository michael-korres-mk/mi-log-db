package com.mikorsoft.milogdb.model;

import java.time.LocalDateTime;

public interface UserLogDTO {
	Long getID();

	LocalDateTime getTimestamp();

	Long getQueryID();

	String getFilters();


}
