package org.shboland.persistence.db.repo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * The base DAO for doing Hibernate actions.
 *
 * @param <T>
 *            The domain object that is managed by this DAO.
 */
@Repository
public abstract class AbstractHibernateRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    /** The type of class the DAO is managing. */
    protected Class<T> domainClass = getDomainClass();

    /**
     * Method to return the class of the domain object.
     *
     * @return Returns a new domain object of the specified class type.
     */
    protected abstract Class<T> getDomainClass();

    protected CriteriaBuilder getDefaultCriteria() {
        return entityManager.getCriteriaBuilder();
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
