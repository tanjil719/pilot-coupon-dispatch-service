package com.pilotcoupondispatchservice.modules.bookings.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.PaymentStatus;
import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;
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
        name = "bookings",
        indexes = {
                @Index(name = "idx_bookings_owner_id", columnList = "owner_id"),
                @Index(name = "idx_bookings_status", columnList = "status"),
                @Index(name = "idx_bookings_vehicle_id", columnList = "vehicle_id"),
                @Index(name = "idx_bookings_service_start", columnList = "service_start"),
                @Index(name = "idx_bookings_pilot_id", columnList = "pilot_id")
        }
)
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "service_start", nullable = false)
    private LocalDateTime serviceStart;

    @Column(name = "service_end", nullable = false)
    private LocalDateTime serviceEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.PENDING_APPROVAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(length = 500)
    private String note;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pilot_id")
    private Pilot pilot;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by", length = 100)
    private String assignedBy;

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
        if (this.status == null) {
            this.status = BookingStatus.PENDING_APPROVAL;
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.UNPAID;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
