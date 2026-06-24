    package com.example.hotel_booking_system.entity;


    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Table;
    import lombok.Getter;
    import lombok.Setter;
    import jakarta.persistence.*;

    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 100)
        private String email;

        @Column(name = "password_hash", nullable = false, length = 255)
        private String passwordHash;

        @Column(name = "full_name", nullable = false, length = 100)
        private String fullName;

        @Column(length = 20)
        private String phone;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive = true;

        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @PrePersist
        public void prePersist() {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
            if (isActive == null) {
                isActive = true;
            }
        }

        @PreUpdate
        public void preUpdate() {
            updatedAt = LocalDateTime.now();
        }
    }
