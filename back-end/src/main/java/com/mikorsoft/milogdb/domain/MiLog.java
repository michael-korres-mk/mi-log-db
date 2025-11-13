package com.mikorsoft.milogdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "mi_logs")
public class MiLog {

	@Id
	private Long id;
	private String IP;
	private String destinationIP;
	private LocalDateTime timestamp;
	private String username;
	private String userID;
	private String httpMethod;
	private String resourceRequested;
	private Long responseSize;
	private String referrer;
	private String userAgent;

	@Enumerated(value = EnumType.STRING)
	private HttpStatus httpStatus;
}