package com.example.practiceauth2.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    private static final String DEFAULT_INSERT_ID = "system";
    private static final String DEFAULT_MODIFY_ID = "system";

    @CreatedDate
    @Column(name = "insert_date", nullable = false, updatable = false)
    private LocalDateTime insertDate = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @CreatedBy
    @Column(name = "insert_id", nullable = false, updatable = false)
    private String insertId = "system";

    @LastModifiedBy
    @Column(name = "modify_id")
    private String modifyId;

    @PrePersist
    protected void onCreate() {
        insertDate = LocalDateTime.now();

        // Or fetch the current user from AuditorAware
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            insertId = authentication.getName();
//        } else {
//            insertId = DEFAULT_INSERT_ID;
//        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = LocalDateTime.now();

        // Or fetch the current user from AuditorAware
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            modifyId = authentication.getName();
//        } else {
//            modifyId = DEFAULT_MODIFY_ID;
//        }
    }
}