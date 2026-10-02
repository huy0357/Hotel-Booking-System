-- =====================================================
-- CỤM 4: SUPPORT & OUTBOX
-- =====================================================

CREATE TABLE outbox_events (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    aggregate_type VARCHAR(50)  NOT NULL,  -- VD: BOOKING
    aggregate_id   VARCHAR(50)  NOT NULL,  -- VD: mã booking
    event_type     VARCHAR(100) NOT NULL,  -- BOOKING_CREATED, BOOKING_CANCELLED...
    payload        JSON         NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING',  -- PENDING, PROCESSED, FAILED
    retry_count    INT          NOT NULL DEFAULT 0,
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at   DATETIME     NULL,
    PRIMARY KEY (id),
    INDEX idx_outbox_status_created (status, created_at),
    INDEX idx_outbox_aggregate (aggregate_type, aggregate_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE audit_logs (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    user_id       BIGINT       NULL,
    action        VARCHAR(100) NOT NULL,
    target_entity VARCHAR(100),
    target_id     VARCHAR(100),
    old_value     JSON         NULL,
    new_value     JSON         NULL,
    ip_address    VARCHAR(50),
    trace_id      VARCHAR(100),  -- MDC TraceId
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_audit_user (user_id),
    INDEX idx_audit_target (target_entity, target_id),
    INDEX idx_audit_trace (trace_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE email_logs (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    recipient     VARCHAR(100) NOT NULL,
    template      VARCHAR(100) NOT NULL,
    status        VARCHAR(20),  -- SENT, FAILED
    error_message TEXT,
    sent_at       DATETIME     NULL,
    PRIMARY KEY (id),
    INDEX idx_email_logs_recipient (recipient)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
