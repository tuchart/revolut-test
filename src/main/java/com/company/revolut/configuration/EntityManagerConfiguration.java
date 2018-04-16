package com.company.revolut.configuration;

import org.glassfish.hk2.api.Factory;

import javax.persistence.EntityManager;
import java.util.Objects;

import static javax.persistence.Persistence.createEntityManagerFactory;

public final class EntityManagerConfiguration implements Factory<EntityManager> {

    private static volatile EntityManager entityManager;

    public EntityManager provide() {
        EntityManager localEntityManager = entityManager;
        if (Objects.isNull(localEntityManager)) {
            synchronized (EntityManager.class) {
                localEntityManager = entityManager;
                if (Objects.isNull(localEntityManager)) {
                    entityManager = createEntityManagerFactory("TEST").createEntityManager();
                }
            }
        }
        return entityManager;
    }

    public void dispose(EntityManager em) {
        // not used
    }
}
