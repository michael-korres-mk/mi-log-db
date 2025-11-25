package com.mikorsoft.milogdb.model;

import java.time.LocalDate;

public interface Query4DTO {
    LocalDate getDay();
    String getBlockId();
    Long getCount();
}
