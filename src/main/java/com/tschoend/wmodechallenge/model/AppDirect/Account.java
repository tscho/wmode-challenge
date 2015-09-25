package com.tschoend.wmodechallenge.model.appdirect;

import com.google.common.collect.Lists;
import com.tschoend.wmodechallenge.model.appdirect.constants.EditionCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.SubscriptionStatus;
import com.tschoend.wmodechallenge.model.appdirect.dto.EventBean;
import com.tschoend.wmodechallenge.model.appdirect.dto.OrderBean;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2015-09-22.
 */
@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "account_identifier")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_account_identifier_seq")
    @SequenceGenerator(name = "accounts_account_identifier_seq",
            sequenceName = "accounts_account_identifier_seq",
            allocationSize = 1)
    private long accountIdentifier;

    private SubscriptionStatus status;

    @Column(name = "edition_code")
    private EditionCode editionCode;

    @OneToMany(mappedBy = "account", targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        this.users.add(user);
        user.setAccount(this);
    }

    public static Account fromOrderBean(OrderBean order) {
        Account account = new Account();
        account.setEditionCode(order.getEditionCode());
        account.setStatus(SubscriptionStatus.ACTIVE);

        return account;
    }
}
