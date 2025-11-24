package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.MiLog;
import com.mikorsoft.milogdb.model.Query1DTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface MiLogRepository extends JpaRepository<MiLog, Long> {

	@Query(nativeQuery = true, value = """
        SELECT l.logtype, COUNT(*) AS total
        FROM mi_log_db.mi_logs l
        WHERE l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY l.logtype
        ORDER BY total DESC
    """
	)
	List<Query1DTO> query1(ZonedDateTime from, ZonedDateTime to);



//	@Query(nativeQuery = true, value = """
//		SELECT *
//		FROM log_entry
//		WHERE IP = ANY (string_to_array(ips, ','));
//	""")
//	Optional<MiLog> findByDestinationIP(String IP);

}
