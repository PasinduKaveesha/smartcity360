package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import lk.jiat.smartcity360.persistence.entity.ResidentEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.Optional;

/**
 * Data access operations for ResidentEntity.
 */
@Stateless
public class ResidentDao extends GenericDao<ResidentEntity, Long> {

    public ResidentDao() {
        super(ResidentEntity.class);
    }

    public Optional<ResidentEntity> findByEmail(String email) {
        try {
            ResidentEntity res = em.createQuery(
                "SELECT r FROM ResidentEntity r WHERE r.email = :email", ResidentEntity.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.of(res);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding resident by email: " + email, e);
        }
    }
}
