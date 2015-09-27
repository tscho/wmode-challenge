package com.tschoend.wmodechallenge.model.appdirect.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * Created by tom on 2015-09-21.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserBean implements Serializable {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String firstName;

    private String language;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    private URL openId;

    @NotNull
    private UUID uuid;
}
