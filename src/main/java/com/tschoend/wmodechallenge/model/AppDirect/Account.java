package com.tschoend.wmodechallenge.model.appdirect;

import com.tschoend.wmodechallenge.model.appdirect.constants.EditionCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.SubscriptionStatus;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by tom on 2015-09-22.
 */
@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "account_identifier")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "account_accountidentifier_seq",
            sequenceName = "account_accountidentifer_seq",
            allocationSize = 1)
    private long accountIdentifier;

    private SubscriptionStatus status;

    @Column(name = "edition_code")
    private EditionCode editionCode;
}
