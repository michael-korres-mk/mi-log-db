package com.mikorsoft.milogdb.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mi_logs_hdfs_details")
public class HdfsLogDetails {

    @Id
    private Long id;   // same as MiLog.id

    @OneToOne
    @MapsId
    @JoinColumn(name = "mi_log_id")
    private MiLog miLog;

    // HDFS_DataXceiver | HDFS_FS_Namesystem
    private String destinationIPs;
    private Long blockID;

    @Override
    public String toString() {
        return "HdfsLogDetails{" +
                "id=" + id +
                ", destinationIPs='" + destinationIPs + '\'' +
                ", blockID=" + blockID +
                '}';
    }
}
