package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import lk.jiat.smartcity360.persistence.entity.ZoneEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;

/**
 * Data access operations for ZoneEntity.
 */
@Stateless
public class ZoneDao extends GenericDao<ZoneEntity, Long> {

    public ZoneDao() {
        super(ZoneEntity.class);
    }

    public List<ZoneEntity> findRootZones() {
        try {
            return em.createQuery("SELECT z FROM ZoneEntity z WHERE z.parent IS NULL", ZoneEntity.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding root zones", e);
        }
    }

    public List<ZoneEntity> findByParentId(Long parentId) {
        try {
            return em.createQuery("SELECT z FROM ZoneEntity z WHERE z.parent.id = :parentId", ZoneEntity.class)
                    .setParameter("parentId", parentId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding zones by parent id: " + parentId, e);
        }
    }
}
