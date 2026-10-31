package lk.jiat.smartcity360.persistence.dao;

import jakarta.ejb.Stateless;
import lk.jiat.smartcity360.persistence.entity.StreetlightModelEntity;

/**
 * Data access operations for StreetlightModelEntity.
 */
@Stateless
public class StreetlightModelDao extends GenericDao<StreetlightModelEntity, String> {

    public StreetlightModelDao() {
        super(StreetlightModelEntity.class);
    }
}
