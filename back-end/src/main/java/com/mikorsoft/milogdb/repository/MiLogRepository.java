package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.MiLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiLogRepository extends JpaRepository<MiLog, Long> {

}
