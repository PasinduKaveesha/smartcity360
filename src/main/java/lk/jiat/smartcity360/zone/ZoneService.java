package lk.jiat.smartcity360.zone;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.ZoneDao;
import lk.jiat.smartcity360.persistence.entity.ZoneEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service managing zone hierarchy tree building, power controls, and usage reporting.
 */
@Stateless
public class ZoneService {

    @Inject
    private ZoneDao zoneDao;

    public List<ZoneNodeDto> getTree() {
        List<ZoneEntity> allEntities = zoneDao.findAll();
        Map<Long, List<ZoneEntity>> childrenMap = buildChildrenMap(allEntities);
        List<ZoneNodeDto> rootDtos = new ArrayList<>();

        for (ZoneEntity entity : allEntities) {
            if (entity.getParent() == null) {
                ZoneNode rootNode = buildDomainTree(entity, childrenMap);
                rootDtos.add(toNodeDto(rootNode, entity.getId(), null));
            }
        }
        return rootDtos;
    }

    public ZoneNodeDto createZone(CreateZoneRequest req) {
        ZoneEntity entity = new ZoneEntity();
        entity.setName(req.getName());
        entity.setLevel(req.getLevel());
        entity.setPowered(true);
        entity.setBaseUsageKwh(req.getBaseUsageKwh() != null ? BigDecimal.valueOf(req.getBaseUsageKwh()) : BigDecimal.ZERO);

        if (req.getParentId() != null) {
            ZoneEntity parent = zoneDao.findById(req.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent zone not found: " + req.getParentId()));
            entity.setParent(parent);
        }

        zoneDao.save(entity);
        ZoneNode node = entity.getLevel().equalsIgnoreCase(ZoneLevel.BUILDING.name())
                ? new Building(entity.getId(), entity.getName(), entity.getBaseUsageKwh().doubleValue(), entity.getPowered())
                : new Zone(entity.getId(), entity.getName(), ZoneLevel.valueOf(entity.getLevel()));
        return toNodeDto(node, entity.getId(), req.getParentId());
    }

    public void shutdownZone(Long zoneId) {
        setPowerState(zoneId, false);
    }

    public void restoreZone(Long zoneId) {
        setPowerState(zoneId, true);
    }

    public ZoneReportDto getReport(Long zoneId) {
        List<ZoneEntity> allEntities = zoneDao.findAll();
        Map<Long, List<ZoneEntity>> childrenMap = buildChildrenMap(allEntities);
        ZoneEntity target = zoneDao.findById(zoneId)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + zoneId));

        ZoneNode targetNode = buildDomainTree(target, childrenMap);
        return new ZoneReportDto(target.getId(), target.getName(), targetNode.reportUsage());
    }

    private void setPowerState(Long zoneId, boolean powered) {
        List<ZoneEntity> allEntities = zoneDao.findAll();
        Map<Long, List<ZoneEntity>> childrenMap = buildChildrenMap(allEntities);
        ZoneEntity target = zoneDao.findById(zoneId)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + zoneId));

        ZoneNode targetNode = buildDomainTree(target, childrenMap);
        if (powered) {
            targetNode.restorePower();
        } else {
            targetNode.shutdownPower();
        }
        syncPowerStateToDb(targetNode);
    }

    private void syncPowerStateToDb(ZoneNode node) {
        if (node instanceof Building b && b.getId() != null) {
            zoneDao.findById(b.getId()).ifPresent(e -> {
                e.setPowered(b.isPowered());
                zoneDao.update(e);
            });
        } else if (node instanceof Zone z && z.getId() != null) {
            zoneDao.findById(z.getId()).ifPresent(e -> {
                e.setPowered(z.isPowered());
                zoneDao.update(e);
            });
            for (ZoneNode child : z.getChildren()) {
                syncPowerStateToDb(child);
            }
        }
    }

    private Map<Long, List<ZoneEntity>> buildChildrenMap(List<ZoneEntity> entities) {
        Map<Long, List<ZoneEntity>> map = new HashMap<>();
        for (ZoneEntity e : entities) {
            if (e.getParent() != null) {
                map.computeIfAbsent(e.getParent().getId(), k -> new ArrayList<>()).add(e);
            }
        }
        return map;
    }

    private ZoneNode buildDomainTree(ZoneEntity entity, Map<Long, List<ZoneEntity>> childrenMap) {
        ZoneLevel level = ZoneLevel.valueOf(entity.getLevel());
        if (level == ZoneLevel.BUILDING) {
            return new Building(entity.getId(), entity.getName(), entity.getBaseUsageKwh().doubleValue(), entity.getPowered());
        }

        Zone composite = new Zone(entity.getId(), entity.getName(), level);
        List<ZoneEntity> children = childrenMap.getOrDefault(entity.getId(), List.of());
        for (ZoneEntity child : children) {
            composite.add(buildDomainTree(child, childrenMap));
        }
        return composite;
    }

    private ZoneNodeDto toNodeDto(ZoneNode node, Long id, Long parentId) {
        ZoneNodeDto dto = new ZoneNodeDto();
        dto.setId(id);
        dto.setName(node.getName());
        dto.setLevel(node.getLevel().name());
        dto.setPowered(node.isPowered());
        dto.setEnergyUsageKwh(node.getEnergyUsageKwh());
        dto.setParentId(parentId);

        if (node instanceof Building b) {
            dto.setBaseUsageKwh(b.getBaseUsageKwh());
        } else if (node instanceof Zone z) {
            for (ZoneNode child : z.getChildren()) {
                Long childId = (child instanceof Building b) ? b.getId() : ((Zone) child).getId();
                dto.getChildren().add(toNodeDto(child, childId, id));
            }
        }
        return dto;
    }
}
