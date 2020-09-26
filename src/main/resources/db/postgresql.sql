SET search_path = sample;

CREATE TABLE IF NOT EXISTS trade
(
    id        BIGSERIAL PRIMARY KEY       NOT NULL,
    client_id BIGINT                      NOT NULL,
    exchange  VARCHAR(255)                NOT NULL,
    ticker    VARCHAR(255)                NOT NULL,
    price     DECIMAL                     NOT NULL,
    amount    BIGINT                      NOT NULL,
    date      TIMESTAMP(3) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status    VARCHAR(60)                 NOT NULL,
    sent      BOOLEAN                     NOT NULL DEFAULT false,
    CONSTRAINT amount_positive CHECK (amount > 0),
    CONSTRAINT status_in_set CHECK (status IN ('COMPLETED', 'CANCELLED'))
);