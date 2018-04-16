package com.company.revolut.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "TRANSACTION_ACTION")
public class Transaction {

    @Id
    private BigInteger id;
    private BigInteger senderId;
    private BigInteger receiverId;
    private Currency currency;
    private BigDecimal amount;
}
