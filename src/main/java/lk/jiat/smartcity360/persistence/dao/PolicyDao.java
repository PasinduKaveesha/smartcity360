package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import lk.jiat.smartcity360.persistence.entity.PolicyEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;
import java.util.Optional;

/**
 * Data access operations for PolicyEntity.
 */
@Stateless
public class PolicyDao extends GenericDao<PolicyEntity, Long> {

    public PolicyDao() {
        super(PolicyEntity.class);
    }

    public Optional<PolicyEntity> findByName(String name) {
        try {
            PolicyEntity policy = em.createQuery(
                "SELECT p FROM PolicyEntity p WHERE p.name = :name", PolicyEntity.class)
                .setParameter("name", name)
                .getSingleResult();
            return Optional.of(policy);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding policy by name: " + name, e);
        }
    }

    public List<PolicyEntity> findAllEnabled() {
        try {
            return em.createQuery("SELECT p FROM PolicyEntity p WHERE p.enabled = true ORDER BY p.id ASC", PolicyEntity.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding enabled policies", e);
        }
    }
}
