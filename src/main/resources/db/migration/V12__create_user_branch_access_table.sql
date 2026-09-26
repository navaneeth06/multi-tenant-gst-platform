CREATE TABLE user_branch_access
(
    user_id    UUID                     NOT NULL,
    branch_id  UUID                     NOT NULL,
    granted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_branch_access
        PRIMARY KEY (user_id, branch_id),

    CONSTRAINT fk_user_branch_access_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_branch_access_branch
        FOREIGN KEY (branch_id)
            REFERENCES branches (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_user_branch_access_branch_id
    ON user_branch_access (branch_id);