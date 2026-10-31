package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import lk.jiat.smartcity360.persistence.entity.SensorReadingEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;
import java.util.Optional;

/**
 * Data access operations for SensorReadingEntity.
 */
@Stateless
public class SensorReadingDao extends GenericDao<SensorReadingEntity, Long> {

    public SensorReadingDao() {
        super(SensorReadingEntity.class);
    }

    public List<SensorReadingEntity> findBySensorId(Long sensorId) {
        try {
            return em.createQuery(
                "SELECT r FROM SensorReadingEntity r WHERE r.sensor.id = :sensorId ORDER BY r.recordedAt DESC",
                SensorReadingEntity.class)
                .setParameter("sensorId", sensorId)
                .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding readings for sensor id: " + sensorId, e);
        }
    }

    public Optional<SensorReadingEntity> findLatestBySensorId(Long sensorId) {
        try {
            List<SensorReadingEntity> list = em.createQuery(
                "SELECT r FROM SensorReadingEntity r WHERE r.sensor.id = :sensorId ORDER BY r.recordedAt DESC",
                SensorReadingEntity.class)
                .setParameter("sensorId", sensorId)
                .setMaxResults(1)
                .getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (Exception e) {
            throw new DataAccessException("Failed finding latest reading for sensor id: " + sensorId, e);
        }
    }

    public Optional<SensorReadingEntity> findLatestByType(String sensorType) {
        try {
            List<SensorReadingEntity> list = em.createQuery(
                "SELECT r FROM SensorReadingEntity r WHERE r.sensor.sensorType = :sensorType ORDER BY r.recordedAt DESC",
                SensorReadingEntity.class)
                .setParameter("sensorType", sensorType)
                .setMaxResults(1)
                .getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (Exception e) {
            throw new DataAccessException("Failed finding latest reading for sensor type: " + sensorType, e);
        }
    }
}
