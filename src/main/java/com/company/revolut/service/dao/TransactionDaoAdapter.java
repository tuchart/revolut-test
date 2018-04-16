package com.company.revolut.service.dao;

import com.company.revolut.dto.Account;
import com.company.revolut.dto.Transaction;
import com.company.revolut.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TransactionDaoAdapter {

    private final EntityManager entityManager;

    @Inject
    public TransactionDaoAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<Transaction> findAll() {
        log.info("findAll.entry");
        return entityManager.createQuery("SELECT transac FROM " + Transaction.class.getSimpleName() + " transac")
                .getResultList();
    }

    public Transaction findOne(BigInteger id) {
        log.info("findOne.entry");
        Transaction transaction = entityManager.find(Transaction.class, id);
        log.debug("findOne.exit transaction={}", transaction);
        return transaction;
    }

    public void makeTransaction(Transaction transaction) {
        log.info("makeTransaction.entry");
        log.debug("makeTransaction.entry transaction={}", transaction);
        Account sender = entityManager.find(Account.class, transaction.getSenderId());
        Account receiver = entityManager.find(Account.class, transaction.getReceiverId());
        if (Objects.isNull(sender) || Objects.isNull(receiver)) {
            throw new BusinessException("Unable to find sender's or receiver's account");
        }
        if (transaction.getAmount().signum() <= 0) {
            throw new BusinessException("Amount must be greater than zero");
        }
        if (sender.getBalance().get(transaction.getCurrency()).subtract(transaction.getAmount()).signum() <= 0) {
            throw new BusinessException("Insufficient funds in sender account");
        }
        entityManager.getTransaction().begin();
        BigDecimal resultBalanceSender = sender.getBalance().get(transaction.getCurrency()).subtract(transaction.getAmount());
        sender.getBalance().replace(transaction.getCurrency(), resultBalanceSender);
        BigDecimal resultBalanceReceiver = receiver.getBalance().get(transaction.getCurrency()).add(transaction.getAmount());
        receiver.getBalance().replace(transaction.getCurrency(), resultBalanceReceiver);
        entityManager.merge(sender);
        entityManager.merge(receiver);
        entityManager.persist(transaction);
        entityManager.getTransaction().commit();
        log.info("makeTransaction.exit");
    }

    public void rollbackTransaction(BigInteger transactionId) {
        log.info("rollbackTransaction.entry");
        Transaction transaction = entityManager.find(Transaction.class, transactionId);
        if (Objects.isNull(transaction)) {
            throw new BusinessException("Unable to find transaction with id=" + transactionId);
        }
        Account sender = entityManager.find(Account.class, transaction.getSenderId());
        Account receiver = entityManager.find(Account.class, transaction.getReceiverId());
        if (Objects.isNull(sender) || Objects.isNull(receiver)) {
            throw new BusinessException("Unable to find sender's or receiver's account");
        }
        entityManager.getTransaction().begin();
        BigDecimal resultBalanceSender = sender.getBalance().get(transaction.getCurrency()).add(transaction.getAmount());
        sender.getBalance().replace(transaction.getCurrency(), resultBalanceSender);
        BigDecimal resultBalanceReceiver = receiver.getBalance().get(transaction.getCurrency()).subtract(transaction.getAmount());
        receiver.getBalance().replace(transaction.getCurrency(), resultBalanceReceiver);
        entityManager.merge(sender);
        entityManager.merge(receiver);
        entityManager.remove(transaction);
        entityManager.getTransaction().commit();
        log.info("rollbackTransaction.exit");
    }
}
