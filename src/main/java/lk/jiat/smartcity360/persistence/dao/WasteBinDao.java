package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import lk.jiat.smartcity360.persistence.entity.WasteBinEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;
import java.util.Optional;

/**
 * Data access operations for WasteBinEntity.
 */
@Stateless
public class WasteBinDao extends GenericDao<WasteBinEntity, Long> {

    public WasteBinDao() {
        super(WasteBinEntity.class);
    }

    public Optional<WasteBinEntity> findByBinCode(String binCode) {
        try {
            WasteBinEntity bin = em.createQuery(
                "SELECT b FROM WasteBinEntity b WHERE b.binCode = :binCode", WasteBinEntity.class)
                .setParameter("binCode", binCode)
                .getSingleResult();
            return Optional.of(bin);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding waste bin by code: " + binCode, e);
        }
    }

    public List<WasteBinEntity> findDueForCollection(int thresholdPercent) {
        try {
            return em.createQuery(
                "SELECT b FROM WasteBinEntity b WHERE b.fillPercent >= :threshold ORDER BY b.fillPercent DESC",
                WasteBinEntity.class)
                .setParameter("threshold", thresholdPercent)
                .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding bins due for collection with threshold: " + thresholdPercent, e);
        }
    }
}
