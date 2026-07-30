-- APIGateway Schema (minimal - for logging/audit only)
CREATE TABLE gateway_requests (
    id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(100) NOT NULL UNIQUE,
    method VARCHAR(10),
    path VARCHAR(500),
    status_code INTEGER,
    response_time_ms INTEGER,
    user_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_gateway_requests_user ON gateway_requests(user_id);
CREATE INDEX idx_gateway_requests_created ON gateway_requests(created_at);
