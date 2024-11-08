CREATE TABLE IF NOT EXISTS polls (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT NOT NULL UNIQUE,
    started_at INTEGER NOT NULL DEFAULT now,
    updated_at INTEGER NOT NULL DEFAULT now,
    -- invariant: !open iff exists r in poll_results s.t. r.poll_id = id.
    open BOOLEAN NOT NULL DEFAULT TRUE,
    admin_token TEXT NOT NULL
);

-- invariant: no two intervals intersect each other.
CREATE TABLE IF NOT EXISTS poll_intervals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    poll_id INTEGER NOT NULL REFERENCES polls (id) ON DELETE CASCADE,
    start INTEGER NOT NULL,
    end INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS poll_results (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    poll_id INTEGER NOT NULL REFERENCES polls (id) ON DELETE CASCADE,
    start INTEGER NOT NULL,
    end INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS poll_users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    poll_id INTEGER NOT NULL REFERENCES polls (id) ON DELETE CASCADE,
    user_id TEXT NOT NULL,
    username TEXT NOT NULL,

    UNIQUE (poll_id, user_id)
);

-- invariant: no two intervals intersect each other.
CREATE TABLE IF NOT EXISTS poll_user_intervals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    poll_user_id INTEGER NOT NULL REFERENCES poll_users (id) ON DELETE CASCADE,
    start INTEGER NOT NULL,
    end INTEGER NOT NULL
);
