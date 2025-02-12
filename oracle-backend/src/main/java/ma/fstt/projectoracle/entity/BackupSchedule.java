package ma.fstt.projectoracle.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
public class BackupSchedule {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cronExpression; // e.g., "0 0 2 * * ?" for 2:00 AM
    private Date lastModified;

    // Getters and Setters
}