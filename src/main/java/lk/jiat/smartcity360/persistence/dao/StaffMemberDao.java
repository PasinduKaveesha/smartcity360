package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import lk.jiat.smartcity360.persistence.entity.StaffMemberEntity;
import lk.jiat.smartcity360.persistence.exception.DataAccessException;
import java.util.List;
import java.util.Optional;

/**
 * Data access operations for StaffMemberEntity.
 */
@Stateless
public class StaffMemberDao extends GenericDao<StaffMemberEntity, Long> {

    public StaffMemberDao() {
        super(StaffMemberEntity.class);
    }

    public List<StaffMemberEntity> findByRole(String role) {
        try {
            return em.createQuery("SELECT s FROM StaffMemberEntity s WHERE s.role = :role", StaffMemberEntity.class)
                    .setParameter("role", role)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed finding staff by role: " + role, e);
        }
    }

    public Optional<StaffMemberEntity> findFirstByRole(String role) {
        List<StaffMemberEntity> staff = findByRole(role);
        return staff.isEmpty() ? Optional.empty() : Optional.of(staff.get(0));
    }
}
