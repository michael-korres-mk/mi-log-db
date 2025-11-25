package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.MiLog;
import com.mikorsoft.milogdb.model.Query1DTO;
import com.mikorsoft.milogdb.model.Query2DTO;
import com.mikorsoft.milogdb.model.Query3DTO;
import com.mikorsoft.milogdb.model.Query4DTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface MiLogRepository extends JpaRepository<MiLog, Long> {

	@Query(nativeQuery = true, value = """
        SELECT l.logtype, COUNT(*) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY l.logtype
        ORDER BY count DESC
    """
	)
	List<Query1DTO> query1(ZonedDateTime from, ZonedDateTime to);

	@Query(nativeQuery = true, value = """
        SELECT TO_CHAR(l.timestamp, 'DD') AS day,COUNT(*) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.logtype = :logType AND l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY TO_CHAR(l.timestamp, 'DD')
    """
	)
	List<Query2DTO> query2(String logType, ZonedDateTime from, ZonedDateTime to);

	@Query(nativeQuery = true, value = """
        SELECT l.ip,COUNT(*) AS count
        FROM mi_log_db.mi_logs l
        WHERE CAST(TO_CHAR(l.timestamp, 'DD') AS BIGINT) = :day
        GROUP BY l.ip
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """
	)
	List<Query3DTO> query3(Long day);



	@Query(nativeQuery = true, value = """
        SELECT TO_CHAR(l.timestamp, 'DD') AS day,l.blockid,COUNT(l.blockid) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.blockid IS NOT NULL AND l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY TO_CHAR(l.timestamp, 'DD'),l.blockid
        ORDER BY COUNT(l.blockid) DESC
        LIMIT 5
    """
	)
	List<Query4DTO> query4(ZonedDateTime from, ZonedDateTime to);


//	@Query(nativeQuery = true, value = """
//		SELECT *
//		FROM log_entry
//		WHERE IP = ANY (string_to_array(ips, ','));
//	""")
//	Optional<MiLog> findByDestinationIP(String IP);

}
