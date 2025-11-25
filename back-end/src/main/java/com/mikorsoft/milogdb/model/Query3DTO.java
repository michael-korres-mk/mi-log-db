package com.mikorsoft.milogdb.model;

import java.time.LocalDate;

public interface Query3DTO {
    String getIp();
    LocalDate getDay();
    Long getCount();
}
