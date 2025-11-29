package com.mikorsoft.milogdb.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.mikorsoft.milogdb.config.Constants.BATCH_SIZE;

@Getter
@Setter
@Entity
@Builder
@Table(name = "user_logs")
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_logs_seq")
	@SequenceGenerator(name = "user_logs_seq", sequenceName = "user_logs_seq", allocationSize = BATCH_SIZE)
	private Long id;

	private LocalDateTime timestamp;

	private Long queryId;

	private String filters;

	@ManyToOne
	@JoinColumn(name = "creator")
	private User creator;

}
