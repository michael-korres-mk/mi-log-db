package com.mikorsoft.milogdb.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.mikorsoft.milogdb.config.Constants.BATCH_SIZE;

@Getter
@Setter
@Entity
@Builder
@Table(name = "mi_logs", indexes = {@Index(name = "idx_ip", columnList = "ip"),@Index(name = "idx_timestamp", columnList = "timestamp")})
@NoArgsConstructor
@AllArgsConstructor
public class MiLog {

	// common
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mi_logs_seq")
	@SequenceGenerator(name = "mi_logs_seq", sequenceName = "mi_logs_seq", allocationSize = BATCH_SIZE)
	private Long id;
	private String IP;
	private LocalDateTime timestamp;
	private Long size;
	@Enumerated(value = EnumType.STRING)
	private LogType logType;

	@OneToOne(mappedBy = "miLog", cascade = CascadeType.ALL, orphanRemoval = true)
	private AccessLogDetails accessLogDetails;

	@OneToOne(mappedBy = "miLog", cascade = CascadeType.ALL, orphanRemoval = true)
	private HdfsLogDetails hdfsLogDetails;


	@Override
	public String toString() {
		return "MiLog{" +
				"id=" + id +
				", IP=" + IP +
				", timestamp=" + timestamp +
				", size=" + size +
				", logType=" + logType +
				'}';
	}
}