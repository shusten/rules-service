CREATE TABLE IF NOT EXISTS notificacao (
    subject VARCHAR(255) NOT NULL,
    camera_id VARCHAR(255),
    status VARCHAR(50),
    percentual DOUBLE PRECISION,
    "timestamp" VARCHAR(255)
);