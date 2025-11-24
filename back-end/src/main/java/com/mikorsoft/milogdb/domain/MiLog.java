package com.mikorsoft.milogdb.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "mi_logs")
@NoArgsConstructor
@AllArgsConstructor
public class MiLog {

	// common
	@Id
	private Long id;
	private String IP;
	private ZonedDateTime timestamp;
	private Long size;
	@Enumerated(value = EnumType.STRING)
	private LogType logType;

	// access
	private String remoteName;
	private String userID;
	private String httpMethod;
	private Integer httpStatus;
	private String resourceRequested;
	private String referrer;
	private String userAgent;

	// HDFS_DataXceiver | HDFS_FS_Namesystem
	private String destinationIPs;
	private Long blockID;

	@Override
	public String toString() {
		return "MiLog{" +
				"id=" + id +
				", IP=" + IP +
				", timestamp=" + timestamp +
				", size=" + size +
				", logType=" + logType +
				", remoteName=" + remoteName +
				", userID=" + userID +
				", httpMethod=" + httpMethod +
				", httpStatus=" + httpStatus +
				", resourceRequested=" + resourceRequested +
				", referrer=" + referrer +
				", userAgent=" + userAgent +
				", destinationIPs=" + destinationIPs +
				", blockID=" + blockID +
				'}';
	}
}