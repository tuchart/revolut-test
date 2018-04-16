package com.company.revolut.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

@Data
@Entity
public class Account {

    @Id
    private BigInteger id;
    private String name;
    private Map<Currency, BigDecimal> balance;
}
