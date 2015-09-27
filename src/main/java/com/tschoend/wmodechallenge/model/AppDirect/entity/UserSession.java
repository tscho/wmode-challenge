package com.tschoend.wmodechallenge.model.appdirect.entity;

import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by tom on 2015-09-26.
 */
@Entity
@Getter
@Setter
@Table(name = "user_sessions")
public class UserSession {
    private UUID token;

    @OneToMany
    @JoinColumn(name = "user_id")
    private User user;

    public static UserSession create(User user) {
        UserSession result = new UserSession();
        result.setUser(user);
        result.setToken(UUID.randomUUID());

        return result;
    }
}
