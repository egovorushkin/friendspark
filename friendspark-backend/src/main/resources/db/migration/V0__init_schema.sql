-- Flyway migration: initial schema for friendspark

CREATE SCHEMA IF NOT EXISTS friendspark;
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
SET search_path TO friendspark,public;

-- =============================================
-- Roles & Permissions
-- =============================================
DROP TYPE IF EXISTS user_role;
CREATE TYPE user_role AS ENUM ('USER','VERIFIED','MODERATOR','ADMIN','BANNED');

DROP TYPE IF EXISTS rsvp_status;
CREATE TYPE rsvp_status AS ENUM ('GOING','INTERESTED','NOT_GOING');

CREATE TABLE IF NOT EXISTS friendspark.users
(
    id             UUID PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    firebase_uid   VARCHAR(128)                NOT NULL UNIQUE,
    email          VARCHAR(255)                NOT NULL,
    first_name     VARCHAR(255)                NOT NULL,
    last_name      VARCHAR(255)                NOT NULL,
    photo_url      VARCHAR(512),

    -- Role system
    role           user_role                   NOT NULL DEFAULT 'USER',
    is_verified    BOOLEAN                              DEFAULT FALSE,

    -- Ban system
    is_banned      BOOLEAN                              DEFAULT FALSE,
    banned_reason  TEXT,
    banned_at      TIMESTAMP WITHOUT TIME ZONE,
    banned_by      UUID REFERENCES friendspark.users (id),

    -- Location (geohash + coords)
    geohash        VARCHAR(12)                  DEFAULT '',
    latitude       DOUBLE PRECISION             DEFAULT 0,
    longitude      DOUBLE PRECISION             DEFAULT 0,

    -- Profile
    birth_date     DATE,
    bio            TEXT,
    gender         VARCHAR(20),

    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    last_active_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc')
);
ALTER TABLE friendspark.users
    ADD CONSTRAINT chk_users_gender
        CHECK (
            gender IN (
                       'MALE',
                       'FEMALE',
                       'NON_BINARY',
                       'OTHER',
                       'PREFER_NOT_TO_SAY'
                )
            );

-- =============================================
-- Interests (Many-to-Many)
-- =============================================
CREATE TABLE IF NOT EXISTS friendspark.interests
(
    id         UUID PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    name       VARCHAR(50) UNIQUE          NOT NULL,
    icon_name  VARCHAR(50), -- e.g. "hiking", "camera"
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc')
);

CREATE TABLE IF NOT EXISTS friendspark.user_interests
(
    user_id     UUID REFERENCES friendspark.users (id) ON DELETE CASCADE,
    interest_id UUID REFERENCES friendspark.interests (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, interest_id),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc')
);

INSERT INTO interests (name, icon_name)
VALUES ('Hiking', 'hiking'),
       ('Photography', 'camera'),
       ('Gaming', 'sports_esports'),
       ('Reading', 'book'),
       ('Cooking', 'restaurant'),
       ('Travel', 'flight'),
       ('Music', 'music_note'),
       ('Art', 'palette'),
       ('Sports', 'sports'),
       ('Movies', 'movie'),
       ('Tech', 'code'),
       ('Volunteering', 'volunteer_activism')
ON CONFLICT DO NOTHING;

-- =============================================
-- Events
-- =============================================
CREATE TABLE IF NOT EXISTS friendspark.events
(
    id               UUID PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    creator_id       UUID                        NOT NULL REFERENCES friendspark.users (id) ON DELETE CASCADE,
    title            VARCHAR(100)                NOT NULL,
    description      TEXT,
    geohash          VARCHAR(12)                 NOT NULL DEFAULT '',
    latitude         DOUBLE PRECISION            NOT NULL,
    longitude        DOUBLE PRECISION            NOT NULL,
    event_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    duration_minutes INT                                  DEFAULT 120,
    max_attendees    INT,
    is_public        BOOLEAN                              DEFAULT TRUE,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    is_hidden        BOOLEAN                              DEFAULT FALSE,
    hidden_by        UUID REFERENCES friendspark.users (id),
    hidden_reason    TEXT
);

-- Event RSVPs
CREATE TABLE IF NOT EXISTS friendspark.user_events_rsvps
(
    user_id      UUID REFERENCES friendspark.users (id) ON DELETE CASCADE,
    event_id     UUID REFERENCES friendspark.events (id) ON DELETE CASCADE,
    status       rsvp_status                 NOT NULL DEFAULT 'INTERESTED',
    responded_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    PRIMARY KEY (user_id, event_id)
);

-- Blocks & Reports
CREATE TABLE IF NOT EXISTS friendspark.blocked_users
(
    blocker_id UUID REFERENCES friendspark.users (id) ON DELETE CASCADE,
    blocked_id UUID REFERENCES friendspark.users (id) ON DELETE CASCADE,
    reason     TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    PRIMARY KEY (blocker_id, blocked_id)
);

CREATE TABLE IF NOT EXISTS friendspark.user_reports
(
    id               UUID PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    reporter_id      UUID                        NOT NULL REFERENCES friendspark.users (id),
    reported_user_id UUID                        NOT NULL REFERENCES friendspark.users (id),
    reason           TEXT                        NOT NULL,
    status           VARCHAR(20)                          DEFAULT 'PENDING',
    reviewed_by      UUID REFERENCES friendspark.users (id),
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc')
);
