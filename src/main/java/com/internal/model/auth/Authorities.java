package com.internal.model.auth;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Immutable
@Table(name = "USER_ROLES", schema = "public")
public class Authorities {
    @Id
    @Column(name = "ROLE")
    private String authority;

    @ManyToOne
    @JoinColumn(name = "USERID")
    private User user;

    public String getAuthority() {
        return authority;
    }

    public User getUser() {
        return user;
    }
}
