package com.mikorsoft.milogdb.repository;

import com.mikorsoft.milogdb.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = """
        INSERT INTO mi_log_db.users(username,password)
        VALUES (:username, :password)
        ON CONFLICT (username) DO NOTHING
    """)
	int insert(String username, String password);

}
