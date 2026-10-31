package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import lk.jiat.smartcity360.persistence.entity.MeterReadingEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;

/**
 * Data access operations for MeterReadingEntity.
 */
@Stateless
public class MeterReadingDao extends GenericDao<MeterReadingEntity, Long> {

    public MeterReadingDao() {
        super(MeterReadingEntity.class);
    }

    public List<MeterReadingEntity> findByMeterId(Long meterId) {
        try {
            return em.createQuery(
                "SELECT r FROM MeterReadingEntity r WHERE r.meter.id = :meterId ORDER BY r.recordedAt DESC",
                MeterReadingEntity.class)
                .setParameter("meterId", meterId)
                .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding readings for meter id: " + meterId, e);
        }
    }
}
