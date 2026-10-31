package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import lk.jiat.smartcity360.persistence.entity.SensorEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;
import java.util.Optional;

/**
 * Data access operations for SensorEntity.
 */
@Stateless
public class SensorDao extends GenericDao<SensorEntity, Long> {

    public SensorDao() {
        super(SensorEntity.class);
    }

    public Optional<SensorEntity> findBySensorCode(String sensorCode) {
        try {
            SensorEntity sensor = em.createQuery(
                "SELECT s FROM SensorEntity s WHERE s.sensorCode = :sensorCode", SensorEntity.class)
                .setParameter("sensorCode", sensorCode)
                .getSingleResult();
            return Optional.of(sensor);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding sensor by code: " + sensorCode, e);
        }
    }

    public List<SensorEntity> findByType(String sensorType) {
        try {
            return em.createQuery("SELECT s FROM SensorEntity s WHERE s.sensorType = :sensorType", SensorEntity.class)
                    .setParameter("sensorType", sensorType)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding sensors by type: " + sensorType, e);
        }
    }
}
