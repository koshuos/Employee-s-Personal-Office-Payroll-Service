package com.internal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_roles", schema = "public", catalog = "internal")
public class UserRoles {
    private Integer userRoleId;
    private Users user;
    private String role;

    @Id
    @SequenceGenerator(name="user_roles_user_role_id_seq",sequenceName="user_roles_user_role_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="user_roles_user_role_id_seq")
    @Column(name="user_role_id", unique=true, nullable=false)
    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    @OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userid", foreignKey = @ForeignKey(name = "user_roles_users_userid_fk"))
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoles userRoles = (UserRoles) o;
        return Objects.equals(userRoleId, userRoles.userRoleId) && Objects.equals(user, userRoles.user) && Objects.equals(role, userRoles.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRoleId, user, role);
    }
}
