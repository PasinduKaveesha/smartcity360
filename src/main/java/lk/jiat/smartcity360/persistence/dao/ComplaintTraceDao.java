package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import lk.jiat.smartcity360.persistence.entity.ComplaintTraceEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;

/**
 * Data access operations for ComplaintTraceEntity.
 */
@Stateless
public class ComplaintTraceDao extends GenericDao<ComplaintTraceEntity, Long> {

    public ComplaintTraceDao() {
        super(ComplaintTraceEntity.class);
    }

    public List<ComplaintTraceEntity> findByComplaintId(Long complaintId) {
        try {
            return em.createQuery(
                "SELECT t FROM ComplaintTraceEntity t WHERE t.complaint.id = :complaintId ORDER BY t.stepNo ASC",
                ComplaintTraceEntity.class)
                .setParameter("complaintId", complaintId)
                .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding traces for complaint id: " + complaintId, e);
        }
    }
}
