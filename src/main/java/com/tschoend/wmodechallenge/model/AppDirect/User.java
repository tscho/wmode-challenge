package com.tschoend.wmodechallenge.model.appdirect;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.security.Principal;
import java.util.UUID;

/**
 * Created by tom on 2015-09-22.
 */
@Data
@Entity
@Table(name = "users")
public class User implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID uuid;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    private String language;

    @NotNull
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "open_id")
    private URL openId;

    @NotNull
    private String role;

    @Override
    public String getName() {
        return firstName + lastName;
    }
}
