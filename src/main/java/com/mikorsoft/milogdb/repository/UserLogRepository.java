package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.UserLog;
import com.mikorsoft.milogdb.model.UserLogDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {

	@Query(nativeQuery = true, value = """
		SELECT *
		FROM mi_log_db.user_logs ul
		JOIN mi_log_db.users u ON ul.creator = u.id
		WHERE u.username = :username
		ORDER BY ul.timestamp DESC
	""")
	List<UserLogDTO> findByUsername(String username);
}
