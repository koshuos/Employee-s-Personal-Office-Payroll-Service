package com.internal.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.Objects;

@Entity
@Table(name = "verificationtoken_password", schema = "public", catalog = "internal")
public class VerificationtokenPassword implements Serializable {
    private static final int EXPIRATION = 60 * 24;
    private long id;
    private Date expirydate;
    private String token;
    private Users user;

    public VerificationtokenPassword(String token, Users user) {
        super();
        this.expirydate = calculateExpiryDate();
        this.token = token;
        this.user = user;
    }

    public VerificationtokenPassword() {
        super();
    }

    @OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "users", foreignKey = @ForeignKey(name = "verificationtoken_password_users_userid_fk"))
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Id
    @SequenceGenerator(name = "verificationtoken_password_id_seq", sequenceName = "verificationtoken_password_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verificationtoken_password_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "expirydate")
    public Date getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(Date expirydate) {
        this.expirydate = expirydate;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerificationtokenPassword that = (VerificationtokenPassword) o;
        return id == that.id && Objects.equals(expirydate, that.expirydate) && Objects.equals(token, that.token) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expirydate, token, user);
    }

    private java.util.Date calculateExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new java.util.Date().getTime());
        cal.add(Calendar.MINUTE, VerificationtokenPassword.EXPIRATION);
        return new java.util.Date(cal.getTime().getTime());
    }
}
