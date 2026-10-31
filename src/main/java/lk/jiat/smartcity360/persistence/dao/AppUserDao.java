package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import lk.jiat.smartcity360.persistence.entity.AppUserEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.Optional;

/**
 * Data access operations for AppUserEntity.
 */
@Stateless
public class AppUserDao extends GenericDao<AppUserEntity, Long> {

    public AppUserDao() {
        super(AppUserEntity.class);
    }

    public Optional<AppUserEntity> findByUsername(String username) {
        try {
            AppUserEntity user = em.createQuery(
                "SELECT u FROM AppUserEntity u WHERE u.username = :username", AppUserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding user by username: " + username, e);
        }
    }
}
