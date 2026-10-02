    package com.example.hotel_booking_system.entity;


    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Table;
    import lombok.Getter;
    import lombok.Setter;
    import jakarta.persistence.*;

    import lombok.*;
    import java.time.LocalDateTime;
    import java.util.HashSet;
    import java.util.Set;

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

        @Column(nullable = false, name = "full_name", length = 100)
        private String fullName;

        @Column(length = 20)
        private String phone;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive = true;

        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Column(name = "avatar_url")
        private String avatarUrl;

        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        @PrePersist
        public void prePersist() {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
            if (isActive == null) {
                isActive = true;
            }
        }

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        @Builder.Default
        private Set<Role> roles = new HashSet<>();

        @PreUpdate
        public void preUpdate() {
            updatedAt = LocalDateTime.now();
        }
    }
