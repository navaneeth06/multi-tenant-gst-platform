package com.navaneeth.multitenantgstplatform.feature.authorization.entity;

import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_branch_access")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserBranchAccess.UserBranchAccessId.class)
public class UserBranchAccess {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @CreationTimestamp
    @Column(name = "granted_at", nullable = false, updatable = false)
    private OffsetDateTime grantedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBranchAccessId implements Serializable {
        private UUID user;
        private UUID branch;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserBranchAccessId that)) return false;
            return Objects.equals(user, that.user) && Objects.equals(branch, that.branch);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, branch);
        }
    }
}