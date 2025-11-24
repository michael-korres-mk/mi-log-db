package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.MiLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiLogRepository extends JpaRepository<MiLog, Long> {


//	@Query(nativeQuery = true, value = """
//		SELECT *
//		FROM log_entry
//		WHERE IP = ANY (string_to_array(ips, ','));
//	""")
//	Optional<MiLog> findByDestinationIP(String IP);

}
