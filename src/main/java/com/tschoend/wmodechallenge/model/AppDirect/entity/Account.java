package com.tschoend.wmodechallenge.model.appdirect.entity;

import com.tschoend.wmodechallenge.model.appdirect.constants.EditionCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.SubscriptionStatus;
import com.tschoend.wmodechallenge.model.appdirect.dto.CompanyBean;
import com.tschoend.wmodechallenge.model.appdirect.dto.MarketPlaceBean;
import com.tschoend.wmodechallenge.model.appdirect.dto.OrderBean;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2015-09-22.
 */
@Getter
@Setter
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

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "marketplace_name")
    private String marketplaceName;

    @Column(name = "marketplace_base_url")
    private URI marketplaceBaseUrl;

    public void addUser(User user) {
        this.users.add(user);
        user.setAccount(this);
    }

    public static Account fromEventBean(OrderBean order, MarketPlaceBean marketPlace, CompanyBean company) {
        Account account = new Account();
        account.setEditionCode(order.getEditionCode());
        account.setStatus(SubscriptionStatus.ACTIVE);
        account.setMarketplaceName(marketPlace.getPartner());
        account.setMarketplaceBaseUrl(marketPlace.getBaseUrl());
        account.setCompanyName(company.getName());

        return account;
    }
}
