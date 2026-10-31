package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import lk.jiat.smartcity360.persistence.entity.EnergyMeterEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.Optional;

/**
 * Data access operations for EnergyMeterEntity.
 */
@Stateless
public class EnergyMeterDao extends GenericDao<EnergyMeterEntity, Long> {

    public EnergyMeterDao() {
        super(EnergyMeterEntity.class);
    }

    public Optional<EnergyMeterEntity> findByMeterCode(String meterCode) {
        try {
            EnergyMeterEntity meter = em.createQuery(
                "SELECT m FROM EnergyMeterEntity m WHERE m.meterCode = :meterCode", EnergyMeterEntity.class)
                .setParameter("meterCode", meterCode)
                .getSingleResult();
            return Optional.of(meter);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding meter by code: " + meterCode, e);
        }
    }
}
