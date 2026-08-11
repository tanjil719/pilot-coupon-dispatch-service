package com.pilotcoupondispatchservice.modules.routes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "routes",
        indexes = {
                @Index(name = "idx_routes_active", columnList = "active")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_routes_start_end", columnNames = {"start_point", "end_point"})
        }
)
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_code", nullable = false, length = 30, unique = true)
    private String routeCode;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "start_point", nullable = false, length = 120)
    private String startPoint;

    @Column(name = "end_point", nullable = false, length = 120)
    private String endPoint;

    @Column(name = "distance_km", nullable = false)
    private Double distanceKm;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "service_fee", nullable = false)
    private Double serviceFee;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

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

    // TODO(next-module): once the Booking module exists, enable the inverse side of the relation.
    // Booking will own a ManyToOne Route field AND its own copied feeAmount column, so changing a
    // route's price later never rewrites the price of past bookings.
    //
    // @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    // private List<Booking> bookings;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = SecurityUtil.getLoggedInUserId();
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = SecurityUtil.getLoggedInUserId();
    }
}
