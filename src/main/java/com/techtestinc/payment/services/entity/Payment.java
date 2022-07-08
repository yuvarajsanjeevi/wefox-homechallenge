package com.techtestinc.payment.services.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "payments")
public class Payment implements Serializable {

    private static final long serialVersionUID = -8288267994496028797L;

    @Id
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @ManyToOne
    @JoinColumn(name="account_id", nullable = false)
    private Account account;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "credit_card")
    private String creditCard;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "created_on", insertable = false)
    private Date createdOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        return getPaymentId().equals(payment.getPaymentId());

    }

    @Override
    public int hashCode() {
        return getPaymentId().hashCode();
    }
}
