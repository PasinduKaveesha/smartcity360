package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import lk.jiat.smartcity360.persistence.entity.StreetlightEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;

/**
 * Data access operations for StreetlightEntity including pagination and batch inserts.
 */
@Stateless
public class StreetlightDao extends GenericDao<StreetlightEntity, Long> {

    public StreetlightDao() {
        super(StreetlightEntity.class);
    }

    public List<StreetlightEntity> findPaged(Long zoneId, int page, int size) {
        try {
            String jpql = zoneId != null
                    ? "SELECT s FROM StreetlightEntity s WHERE s.zone.id = :zoneId ORDER BY s.id ASC"
                    : "SELECT s FROM StreetlightEntity s ORDER BY s.id ASC";

            TypedQuery<StreetlightEntity> query = em.createQuery(jpql, StreetlightEntity.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size);

            if (zoneId != null) {
                query.setParameter("zoneId", zoneId);
            }
            return query.getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed retrieving paged streetlights", e);
        }
    }

    public long countByZone(Long zoneId) {
        try {
            if (zoneId == null) {
                return count();
            }
            return em.createQuery("SELECT COUNT(s) FROM StreetlightEntity s WHERE s.zone.id = :zoneId", Long.class)
                    .setParameter("zoneId", zoneId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DataAccessException("Failed counting streetlights for zone " + zoneId, e);
        }
    }

    public void batchInsert(List<StreetlightEntity> lights, int batchSize) {
        try {
            for (int i = 0; i < lights.size(); i++) {
                em.persist(lights.get(i));
                if ((i + 1) % batchSize == 0) {
                    em.flush();
                    em.clear();
                }
            }
            em.flush();
            em.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed batch inserting streetlights", e);
        }
    }
}
