package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import lk.jiat.smartcity360.persistence.entity.PolicyLogEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;

/**
 * Data access operations for PolicyLogEntity.
 */
@Stateless
public class PolicyLogDao extends GenericDao<PolicyLogEntity, Long> {

    public PolicyLogDao() {
        super(PolicyLogEntity.class);
    }

    public List<PolicyLogEntity> findRecentLogs(int maxResults) {
        try {
            return em.createQuery("SELECT l FROM PolicyLogEntity l ORDER BY l.evaluatedAt DESC", PolicyLogEntity.class)
                    .setMaxResults(maxResults)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding recent policy logs", e);
        }
    }
}
