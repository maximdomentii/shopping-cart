package com.zavod.shoppingcart.authservice.security.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_authorization")
public class UserAuthorization implements Serializable {

    private static final long serialVersionUID = 8309696455872015726L;

    @Id
    @GeneratedValue
    private long userRoleId;

    private String role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAuthentication user;

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserAuthentication getUser() {
        return user;
    }

    public void setUser(UserAuthentication user) {
        this.user = user;
    }
}
