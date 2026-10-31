package lk.jiat.smartcity360.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Generic base DAO providing standard CRUD operations using JPA EntityManager.
 *
 * @param <T>  Entity type
 * @param <ID> Primary key type
 */
public abstract class GenericDao<T, ID extends Serializable> {

    @PersistenceContext(unitName = "smartcity360PU")
    protected EntityManager em;

    private final Class<T> entityClass;

    protected GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Optional<T> findById(ID id) {
        try {
            return Optional.ofNullable(em.find(entityClass, id));
        } catch (Exception e) {
            throw new DataAccessException("Failed finding " + entityClass.getSimpleName() + " by id: " + id, e);
        }
    }

    public List<T> findAll() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed retrieving all records for " + entityClass.getSimpleName(), e);
        }
    }

    public T save(T entity) {
        try {
            em.persist(entity);
            return entity;
        } catch (Exception e) {
            throw new DataAccessException("Failed persisting entity " + entityClass.getSimpleName(), e);
        }
    }

    public T update(T entity) {
        try {
            return em.merge(entity);
        } catch (Exception e) {
            throw new DataAccessException("Failed merging entity " + entityClass.getSimpleName(), e);
        }
    }

    public void delete(T entity) {
        try {
            T managed = em.contains(entity) ? entity : em.merge(entity);
            em.remove(managed);
        } catch (Exception e) {
            throw new DataAccessException("Failed deleting entity " + entityClass.getSimpleName(), e);
        }
    }

    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }

    public long count() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            cq.select(cb.count(cq.from(entityClass)));
            return em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            throw new DataAccessException("Failed counting " + entityClass.getSimpleName(), e);
        }
    }
}
