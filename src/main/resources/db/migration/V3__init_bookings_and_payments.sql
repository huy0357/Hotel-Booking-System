-- =====================================================
-- CỤM 3: BOOKING, PAYMENT & PROMOTION
-- =====================================================

CREATE TABLE promotions (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    code           VARCHAR(50)   NOT NULL,
    discount_type  VARCHAR(20)   NOT NULL,  -- PERCENTAGE, FIXED_AMOUNT
    discount_value DECIMAL(12,2) NOT NULL,
    start_date     DATETIME      NOT NULL,
    end_date       DATETIME      NOT NULL,
    usage_limit    INT,
    used_count     INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uq_promotions_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE bookings (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    booking_code    VARCHAR(50)   NOT NULL,
    user_id         BIGINT        NOT NULL,
    hotel_id        BIGINT        NOT NULL,
    promotion_id    BIGINT        NULL,
    status          VARCHAR(50)   NOT NULL,  -- PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED, EXPIRED
    total_price     DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) NOT NULL DEFAULT 0.0,
    final_price     DECIMAL(12,2) NOT NULL,
    hold_expires_at DATETIME      NULL,      -- Giữ chỗ 10 phút
    version         BIGINT        NOT NULL DEFAULT 0,  -- Optimistic Lock
    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_bookings_code UNIQUE (booking_code),
    CONSTRAINT fk_bookings_user      FOREIGN KEY (user_id)      REFERENCES users (id),
    CONSTRAINT fk_bookings_hotel     FOREIGN KEY (hotel_id)     REFERENCES hotels (id),
    CONSTRAINT fk_bookings_promotion FOREIGN KEY (promotion_id) REFERENCES promotions (id),
    INDEX idx_bookings_user (user_id),
    INDEX idx_bookings_hotel (hotel_id),
    INDEX idx_bookings_status_hold (status, hold_expires_at)  -- Job quét booking hết hạn giữ chỗ
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE booking_rooms (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    booking_id      BIGINT        NOT NULL,
    room_id         BIGINT        NOT NULL,
    check_in_date   DATE          NOT NULL,
    check_out_date  DATE          NOT NULL,
    price_per_night DECIMAL(12,2) NOT NULL,  -- Price Snapshot
    is_active       BOOLEAN       NOT NULL DEFAULT TRUE,  -- FALSE khi huỷ/hết hạn để nhả phòng
    PRIMARY KEY (id),
    CONSTRAINT fk_booking_rooms_booking FOREIGN KEY (booking_id) REFERENCES bookings (id),
    CONSTRAINT fk_booking_rooms_room    FOREIGN KEY (room_id)    REFERENCES rooms (id),
    INDEX idx_booking_rooms_booking (booking_id),
    INDEX idx_booking_rooms_availability (room_id, check_in_date, check_out_date, is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE payments (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    booking_id      BIGINT        NOT NULL,
    idempotency_key VARCHAR(100)  NOT NULL,  -- Chống thanh toán trùng
    amount          DECIMAL(12,2) NOT NULL,
    method          VARCHAR(50),             -- CASH, VNPAY, MOMO, CREDIT_CARD
    status          VARCHAR(50),             -- PENDING, SUCCESS, FAILED
    transaction_id  VARCHAR(100),            -- Mã đối soát từ cổng thanh toán
    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_payments_idempotency UNIQUE (idempotency_key),
    CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings (id),
    INDEX idx_payments_booking (booking_id),
    INDEX idx_payments_transaction (transaction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reviews (
    id         BIGINT   NOT NULL AUTO_INCREMENT,
    booking_id BIGINT   NOT NULL,
    rating     INT      NOT NULL,
    comment    TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_reviews_booking UNIQUE (booking_id),
    CONSTRAINT fk_reviews_booking FOREIGN KEY (booking_id) REFERENCES bookings (id),
    CONSTRAINT chk_reviews_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
