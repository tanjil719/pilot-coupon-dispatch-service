package com.pilotcoupondispatchservice.modules.pilots.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pilotcoupondispatchservice.enums.PilotStatus;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "pilots",
        indexes = {
                @Index(name = "idx_pilots_status", columnList = "status")
        }
)
public class Pilot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pilot_code", nullable = false, length = 30, unique = true)
    private String pilotCode;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "license_no", nullable = false, length = 50, unique = true)
    private String licenseNo;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PilotStatus status = PilotStatus.AVAILABLE;

    @JsonProperty(access = READ_ONLY)
    private long createdBy;

    @JsonProperty(access = READ_ONLY)
    private long updatedBy;

    @JsonProperty(access = READ_ONLY)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonProperty(access = READ_ONLY)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = SecurityUtil.getLoggedInUserId();
        if (this.status == null) {
            this.status = PilotStatus.AVAILABLE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = SecurityUtil.getLoggedInUserId();
    }
}
