-- =====================================================
-- CỤM 2: HOTEL & ROOM CATALOG
-- =====================================================

CREATE TABLE hotels (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    name           VARCHAR(200) NOT NULL,
    city           VARCHAR(100) NOT NULL,
    address        VARCHAR(255) NOT NULL,
    description    TEXT,
    rating_average DECIMAL(2,1) NOT NULL DEFAULT 0.0,
    is_active      BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted_at     DATETIME     NULL,
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_hotels_city (city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE room_types (
    id          BIGINT         NOT NULL AUTO_INCREMENT,
    hotel_id    BIGINT         NOT NULL,
    name        VARCHAR(100)   NOT NULL,
    base_price  DECIMAL(12,2)  NOT NULL,
    capacity    INT            NOT NULL,
    description TEXT,
    deleted_at  DATETIME       NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_room_types_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id),
    INDEX idx_room_types_hotel (hotel_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE rooms (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    room_type_id BIGINT       NOT NULL,
    room_number  VARCHAR(20)  NOT NULL,
    status       VARCHAR(50)  NOT NULL DEFAULT 'AVAILABLE',  -- AVAILABLE, MAINTENANCE
    version      BIGINT       NOT NULL DEFAULT 0,             -- Optimistic Lock
    deleted_at   DATETIME     NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_rooms_room_type FOREIGN KEY (room_type_id) REFERENCES room_types (id),
    CONSTRAINT uq_rooms_type_number UNIQUE (room_type_id, room_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE amenities (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    icon_url VARCHAR(500),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE room_type_amenities (
    room_type_id BIGINT NOT NULL,
    amenity_id   BIGINT NOT NULL,
    PRIMARY KEY (room_type_id, amenity_id),
    CONSTRAINT fk_rta_room_type FOREIGN KEY (room_type_id) REFERENCES room_types (id),
    CONSTRAINT fk_rta_amenity   FOREIGN KEY (amenity_id)   REFERENCES amenities (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE room_images (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    room_type_id BIGINT       NOT NULL,
    image_url    VARCHAR(500) NOT NULL,
    is_primary   BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_room_images_room_type FOREIGN KEY (room_type_id) REFERENCES room_types (id),
    INDEX idx_room_images_room_type (room_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE seasonal_prices (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    room_type_id BIGINT        NOT NULL,
    start_date   DATE          NOT NULL,
    end_date     DATE          NOT NULL,
    price        DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_seasonal_prices_room_type FOREIGN KEY (room_type_id) REFERENCES room_types (id),
    INDEX idx_seasonal_prices_range (room_type_id, start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
