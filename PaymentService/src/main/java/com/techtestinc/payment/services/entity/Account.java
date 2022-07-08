package com.techtestinc.payment.services.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class Account implements Serializable {

    private static final long serialVersionUID = 7727633271170201341L;

    @Id
    @Column(name = "account_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birthdate")
    private Date birthDate;

    @Column(name = "last_payment_date")
    private Date lastPaymentDate;

    @Column(name = "created_on")
    private Date createdOn;

    @OneToMany(mappedBy = "account", targetEntity = Payment.class, fetch = FetchType.LAZY)
    private Set<Payment> payments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return getAccountId() != null ? getAccountId().equals(account.getAccountId()) : account.getAccountId() == null;

    }

    @Override
    public int hashCode() {
        return getAccountId() != null ? getAccountId().hashCode() : 0;
    }
}
