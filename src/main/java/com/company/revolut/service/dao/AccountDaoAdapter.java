package com.company.revolut.service.dao;

import com.company.revolut.dto.Account;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Slf4j
public class AccountDaoAdapter {

    private final EntityManager entityManager;

    @Inject
    public AccountDaoAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<Account> getAllAccounts() {
        log.info("getAllAccounts.entry");
        return entityManager.createQuery("SELECT acc FROM " + Account.class.getSimpleName() + " acc").getResultList();
    }

    public Account createAccount(Account account) {
        log.info("createAccount.entry");
        log.debug("createAccount.entry account={}", account);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        return entityManager.find(Account.class, account.getId());
    }

    public Account findOne(BigInteger id) {
        log.info("findOne.entry id={}", id);
        Account account = entityManager.find(Account.class, id);
        log.debug("findOne.exit account={}", account);
        return account;
    }

    public void delete(BigInteger id) {
        entityManager.getTransaction().begin();
        log.info("delete.entry id={}", id);
        Account account = entityManager.find(Account.class, id);
        log.debug("delete.found account={}", account);
        if (Objects.isNull(account)) {
            return;
        }
        entityManager.remove(account);
        entityManager.getTransaction().commit();
    }

    public Account update(Account account) {
        entityManager.getTransaction().begin();
        log.info("update.entry id={}", account.getId());
        Account oldAccount = entityManager.find(Account.class, account.getId());
        log.debug("update.found account={}", account);
        if (Objects.isNull(oldAccount)) {
            return null;
        }
        entityManager.merge(account);
        entityManager.getTransaction().commit();
        return account;
    }
}
