package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.MiLog;
import com.mikorsoft.milogdb.model.QueryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MiLogRepository extends JpaRepository<MiLog, Long> {

	@Query(nativeQuery = true, value = """
        SELECT l.logtype, COUNT(*) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY l.logtype
        ORDER BY count DESC
    """)
	List<QueryDTO> query1(LocalDateTime from, LocalDateTime to);

	@Query(nativeQuery = true, value = """
        SELECT CAST(l.timestamp AS date) AS day,COUNT(*) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.logtype = :logType AND l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY CAST(l.timestamp AS date)
    """)
	List<QueryDTO> query2(String logType, LocalDateTime from, LocalDateTime to);

	@Query(nativeQuery = true, value = """
        SELECT l.ip,COUNT(*) AS count
        FROM mi_log_db.mi_logs l
        WHERE CAST(l.timestamp AS date) = :day
        GROUP BY l.ip
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """)
	List<QueryDTO> query3(LocalDate day);



	@Query(nativeQuery = true, value = """
        SELECT CAST(l.timestamp AS date) AS day,l.blockid,COUNT(l.blockid) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.blockid IS NOT NULL AND l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY CAST(l.timestamp AS date),l.blockid
        ORDER BY COUNT(l.blockid) DESC
        LIMIT 5
    """)
	List<QueryDTO> query4(LocalDateTime from, LocalDateTime to);

	@Query(nativeQuery = true, value = """
		SELECT l.referrer,COUNT(DISTINCT l.resourcerequested) AS count
		FROM mi_log_db.mi_logs l
		WHERE l.referrer IS NOT NULL AND l.referrer <> '-' AND l.resourcerequested IS NOT NULL
		GROUP BY l.referrer
		HAVING COUNT(DISTINCT l.resourcerequested) > 1;
	""")
	List<QueryDTO> query5();

	@Query(nativeQuery = true, value = """
		SELECT l.resourcerequested,COUNT(*) AS count
		FROM mi_log_db.mi_logs l
		WHERE l.resourcerequested IS NOT NULL
		GROUP BY l.resourcerequested
		ORDER BY COUNT(*) DESC
		OFFSET 1 LIMIT 1
	""")
	List<QueryDTO> query6();

	@Query(nativeQuery = true, value = """
		SELECT *
		FROM mi_log_db.mi_logs l
		WHERE l.size IS NOT NULL AND size < :size
	""")
	List<QueryDTO> query7(Long size);

	@Query(nativeQuery = true, value = """
		SELECT l.blockid,CAST(l.timestamp AS date) AS day
		FROM mi_log_db.mi_logs l
		WHERE l.logtype IN ('REPLICATE', 'SERVED')
		GROUP BY l.blockid,CAST(l.timestamp AS date)
		HAVING COUNT(DISTINCT l.logtype) = 2;
	""")
	List<QueryDTO> query8();

	@Query(nativeQuery = true, value = """
		SELECT l.blockid,CAST(l.timestamp AS date) AS day,EXTRACT(HOUR FROM l.timestamp) AS hour
		FROM mi_log_db.mi_logs l
		WHERE l.logtype IN ('REPLICATE', 'SERVED')
		GROUP BY l.blockid,CAST(l.timestamp AS date),EXTRACT(HOUR FROM l.timestamp)
		HAVING COUNT(DISTINCT l.logtype) = 2;
	""")
	List<QueryDTO> query9();

	@Query(nativeQuery = true, value = """
			SELECT *
			FROM mi_log_db.mi_logs l
			WHERE lower(l.useragent) LIKE '%firefox%'
	""")
	List<QueryDTO> query10();

	@Query(nativeQuery = true, value = """
        SELECT l.IP, COUNT(l.httpmethod) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.timestamp >= :from AND l.timestamp <= :to AND l.httpmethod = :httpMethod
        GROUP BY l.IP
    """)
	List<QueryDTO> query11(LocalDateTime from, LocalDateTime to,String httpMethod);

	@Query(nativeQuery = true, value = """
        SELECT l.IP, COUNT(l.httpmethod) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.timestamp >= :from AND l.timestamp <= :to AND l.httpmethod IN :httpMethods
        GROUP BY l.IP
        HAVING COUNT(DISTINCT l.httpmethod) = 2;
    """)
	List<QueryDTO> query12(LocalDateTime from, LocalDateTime to,List<String> httpMethods);

	@Query(nativeQuery = true, value = """
        SELECT l.IP, COUNT(l.httpmethod) AS count
        FROM mi_log_db.mi_logs l
        WHERE l.timestamp >= :from AND l.timestamp <= :to
        GROUP BY l.IP
        HAVING COUNT(DISTINCT l.httpmethod) = 4;
    """)
	List<QueryDTO> query13(LocalDateTime from, LocalDateTime to);


//	@Query(nativeQuery = true, value = """
//		SELECT *
//		FROM log_entry
//		WHERE IP = ANY (string_to_array(ips, ','));
//	""")
//	Optional<MiLog> findByDestinationIP(String IP);

}
