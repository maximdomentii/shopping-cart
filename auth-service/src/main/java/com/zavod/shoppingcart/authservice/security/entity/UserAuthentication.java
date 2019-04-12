package com.zavod.shoppingcart.authservice.security.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user_authentication")
public class UserAuthentication implements Serializable {

    private static final long serialVersionUID = 3339653549547841680L;

    @Id
    @GeneratedValue
    private long userId;

    private String username;

    private String password;

    private boolean enabled;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserAuthorization> roles;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserAuthorization> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserAuthorization> roles) {
        this.roles = roles;
    }
}
