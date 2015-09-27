package com.tschoend.wmodechallenge.model.appdirect.entity;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.UUID;

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

    @Column(name = "accessed_at")
    private DateTime accessedAt;

    public static UserSession create(User user) {
        UserSession result = new UserSession();
        result.setUser(user);
        result.setToken(UUID.randomUUID());
        result.setAccessedAt(DateTime.now());

        return result;
    }
}
