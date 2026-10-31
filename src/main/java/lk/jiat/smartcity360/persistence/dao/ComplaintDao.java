package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import lk.jiat.smartcity360.persistence.entity.ComplaintEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;

/**
 * Data access operations for ComplaintEntity.
 */
@Stateless
public class ComplaintDao extends GenericDao<ComplaintEntity, Long> {

    public ComplaintDao() {
        super(ComplaintEntity.class);
    }

    public List<ComplaintEntity> findAllWithTraces() {
        try {
            return em.createQuery(
                "SELECT DISTINCT c FROM ComplaintEntity c LEFT JOIN FETCH c.traces ORDER BY c.createdAt DESC",
                ComplaintEntity.class)
                .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding complaints with traces", e);
        }
    }
}
