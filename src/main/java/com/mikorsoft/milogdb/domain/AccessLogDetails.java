package com.mikorsoft.milogdb.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mi_logs_access_details")
public class AccessLogDetails {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "mi_log_id")
    private MiLog miLog;

    // access
    private String remoteName;
    private String userID;
    private String httpMethod;
    private Integer httpStatus;
    private String resourceRequested;
    private String referrer;
    private String userAgent;

    @Override
    public String toString() {
        return "AccessLogDetails{" +
                "id=" + id +
                ", remoteName='" + remoteName + '\'' +
                ", userID='" + userID + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", httpStatus=" + httpStatus +
                ", resourceRequested='" + resourceRequested + '\'' +
                ", referrer='" + referrer + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
