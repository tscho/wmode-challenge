package com.tschoend.wmodechallenge.model.appdirect.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;
import javax.persistence.Id;

/**
 * Created by tom on 2015-09-26.
 */
@Entity
@Getter
@Setter
@Table(name = "user_sessions")
public class UserSession {
    @Id
    private UUID token;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public static UserSession create(User user) {
        UserSession result = new UserSession();
        result.setUser(user);
        result.setToken(UUID.randomUUID());

        return result;
    }
}
