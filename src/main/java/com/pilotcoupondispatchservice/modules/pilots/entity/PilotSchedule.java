package com.pilotcoupondispatchservice.modules.pilots.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pilotcoupondispatchservice.enums.ScheduleStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
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
        name = "pilot_schedules",
        indexes = {
                @Index(name = "idx_pilot_schedules_pilot_id", columnList = "pilot_id"),
                @Index(name = "idx_pilot_schedules_pilot_window", columnList = "pilot_id, service_start, service_end")
        }
)
public class PilotSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pilot_id", nullable = false)
    private Pilot pilot;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "route_code", nullable = false, length = 30)
    private String routeCode;

    @Column(name = "service_start", nullable = false)
    private LocalDateTime serviceStart;

    @Column(name = "service_end", nullable = false)
    private LocalDateTime serviceEnd;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

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
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public ScheduleStatus getStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(serviceEnd)) {
            return ScheduleStatus.COMPLETED;
        }
        if (!now.isBefore(serviceStart)) {
            return ScheduleStatus.IN_PROGRESS;
        }
        return ScheduleStatus.SCHEDULED;
    }
}
