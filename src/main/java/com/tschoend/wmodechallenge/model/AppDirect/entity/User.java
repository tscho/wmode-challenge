package com.tschoend.wmodechallenge.model.appdirect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tschoend.wmodechallenge.model.appdirect.dto.UserBean;
import lombok.*;

import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.security.Principal;
import java.util.UUID;

/**
 * Created by tom on 2015-09-22.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1)
    private Long id;

    @JsonIgnore
    private UUID uuid;

    private String email;

    @Column(name = "first_name")
    private String firstName;

    private String language;

    @Column(name = "last_name")
    private String lastName;

    @JsonIgnore
    @Column(name = "open_id")
    private URI openId;

    @JsonIgnore
    private String role;

    @ManyToOne
    @JoinColumn(name = "account_identifier")
    private Account account;

    @Override
    public String getName() {
        return email;
    }

    public Long getAccount() {
        return account.getAccountIdentifier();
    }

    @JsonIgnore
    public Account getAccountEntity() { return account; }

    public static User fromUserBean(UserBean userBean, String role) {
        User user = new User(
                null,
                userBean.getUuid(),
                userBean.getEmail(),
                userBean.getFirstName(),
                userBean.getLanguage(),
                userBean.getLastName(),
                userBean.getOpenId(),
                role,
                null);

        return user;
    }
}
