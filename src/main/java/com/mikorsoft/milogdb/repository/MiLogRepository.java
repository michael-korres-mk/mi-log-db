package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.MiLog;
import com.mikorsoft.milogdb.model.QueryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
		SELECT CAST(l.timestamp AS date) AS day,d.blockid,COUNT(d.blockid) AS count
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_hdfs_details d ON l.id = d.mi_log_id
		WHERE d.blockid IS NOT NULL AND l.timestamp >= :from AND l.timestamp <= :to
		GROUP BY CAST(l.timestamp AS date),d.blockid
		ORDER BY COUNT(d.blockid) DESC
		LIMIT 5
	""")
	List<QueryDTO> query4(LocalDateTime from, LocalDateTime to);

	@Query(nativeQuery = true, value = """
		SELECT d.referrer,COUNT(DISTINCT d.resourcerequested) AS count
		FROM mi_log_db.mi_logs_access_details d
		WHERE d.referrer IS NOT NULL AND d.referrer <> '-' AND d.resourcerequested IS NOT NULL
		GROUP BY d.referrer
		HAVING COUNT(DISTINCT d.resourcerequested) > 1;
	""")
	List<QueryDTO> query5();

	@Query(nativeQuery = true, value = """
		SELECT d.resourcerequested,COUNT(*) AS count
		FROM mi_log_db.mi_logs_access_details d
		WHERE d.resourcerequested IS NOT NULL
		GROUP BY d.resourcerequested
		ORDER BY COUNT(*) DESC
		OFFSET 1 LIMIT 1
	""")
	List<QueryDTO> query6();

	@Query(nativeQuery = true, value = """
		SELECT *
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_access_details d ON l.id = d.mi_log_id
		WHERE l.logtype = 'ACCESS' AND l.size IS NOT NULL AND size < :size
	""")
	List<QueryDTO> query7(Long size);

	@Query(nativeQuery = true, value = """
		SELECT d.blockid,CAST(l.timestamp AS date) AS day
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_hdfs_details d ON l.id = d.mi_log_id
		WHERE l.logtype IN ('REPLICATE', 'SERVED')
		GROUP BY d.blockid,CAST(l.timestamp AS date)
		HAVING COUNT(DISTINCT l.logtype) = 2;
	""")
	List<QueryDTO> query8();

	@Query(nativeQuery = true, value = """
		SELECT d.blockid,CAST(l.timestamp AS date) AS day,EXTRACT(HOUR FROM l.timestamp) AS hour
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_hdfs_details d ON l.id = d.mi_log_id
		WHERE l.logtype IN ('REPLICATE', 'SERVED')
		GROUP BY d.blockid,CAST(l.timestamp AS date),EXTRACT(HOUR FROM l.timestamp)
		HAVING COUNT(DISTINCT l.logtype) = 2;
	""")
	List<QueryDTO> query9();

	@Query(nativeQuery = true, value = """
		SELECT *
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_access_details d ON l.id = d.mi_log_id
		WHERE l.logtype = 'ACCESS' AND lower(d.useragent) LIKE '%firefox%'
	""")
	List<QueryDTO> query10();

	@Query(nativeQuery = true, value = """
		SELECT l.IP, COUNT(d.httpmethod) AS count
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_access_details d ON l.id = d.mi_log_id
		WHERE l.logtype = 'ACCESS' AND l.timestamp >= :from AND l.timestamp <= :to AND d.httpmethod = :httpMethod
		GROUP BY l.IP
	""")
	List<QueryDTO> query11(LocalDateTime from, LocalDateTime to,String httpMethod);

	@Query(nativeQuery = true, value = """
		SELECT l.IP, COUNT(d.httpmethod) AS count
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_access_details d ON l.id = d.mi_log_id
		WHERE l.logtype = 'ACCESS' AND l.timestamp >= :from AND l.timestamp <= :to AND d.httpmethod IN :httpMethods
		GROUP BY l.IP
		HAVING COUNT(DISTINCT d.httpmethod) = 2;
	""")
	List<QueryDTO> query12(LocalDateTime from, LocalDateTime to,List<String> httpMethods);

	@Query(nativeQuery = true, value = """
		SELECT l.IP, COUNT(d.httpmethod) AS count
		FROM mi_log_db.mi_logs l
		JOIN mi_log_db.mi_logs_access_details d ON l.id = d.mi_log_id
		WHERE l.logtype = 'ACCESS' AND l.timestamp >= :from AND l.timestamp <= :to
		GROUP BY l.IP
		HAVING COUNT(DISTINCT d.httpmethod) = 4;
	""")
	List<QueryDTO> query13(LocalDateTime from, LocalDateTime to);

	@Query(nativeQuery = true, value = """
		SELECT *
		FROM mi_log_db.mi_logs l
		LEFT JOIN mi_log_db.mi_logs_hdfs_details d1 ON l.id = d1.mi_log_id
		LEFT JOIN mi_log_db.mi_logs_access_details d2 ON l.id = d2.mi_log_id
		WHERE l.ID = :ID
	""")
	Optional<QueryDTO> findByID(Long ID);

	@Query(nativeQuery = true, value = """
		SELECT *
		FROM mi_log_db.mi_logs l
		LEFT JOIN mi_log_db.mi_logs_hdfs_details d1 ON l.id = d1.mi_log_id
		LEFT JOIN mi_log_db.mi_logs_access_details d2 ON l.id = d2.mi_log_id
		WHERE (:IP = l.IP) OR (d1.destinationips IS NOT NULL AND :IP = ANY (string_to_array(d1.destinationips, ',')));
	""")
	List<QueryDTO> findByIP(String IP);

//	@Modifying
//	@Transactional
//	@Query(nativeQuery = true, value = """
//		INSERT INTO mi_log_db.mi_logs (ip, timestamp, size, logType, remoteName,userID,httpMethod, httpStatus, resourceRequested, referrer, userAgent, destinationIPs, blockID)
//		VALUES (:ip, :timestamp, :size, :logType, :remoteName, :userID, :httpMethod, :httpStatus, :resourceRequested, :referrer, :userAgent, :destinationIPs, :blockID)
//	""")
//	int create(String ip, LocalDateTime timestamp, Long size, String logType, String remoteName, String userID, String httpMethod, Integer httpStatus, String resourceRequested, String referrer, String userAgent, String destinationIPs, Long blockID);
//
//
//	@Modifying
//	@Transactional
//	@Query(nativeQuery = true, value = """
//		UPDATE mi_log_db.mi_logs l
//	    SET IP = :ip,
//			timestamp = :timestamp,
//			size = :size,
//			logType = :logType,
//			remoteName = :remoteName,
//			userID=:userID,
//			httpMethod=:httpMethod,
//			httpStatus = :httpStatus,
//			resourceRequested = :resourceRequested,
//			referrer = :referrer,
//			userAgent = :userAgent,
//			destinationIPs = :destinationIPs,
//			blockID = :blockID
//		WHERE l.ID = :id
//	""")
//	int update(Long id, String ip, LocalDateTime timestamp, Long size, String logType, String remoteName, String userID, String httpMethod, Integer httpStatus, String resourceRequested, String referrer, String userAgent, String destinationIPs, Long blockID);

}
