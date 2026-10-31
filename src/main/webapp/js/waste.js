/**
 * Waste management UI controller.
 */
async function loadWasteBins() {
    try {
        const bins = await apiFetch('waste/bins');
        const tbody = document.getElementById('waste-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        bins.forEach(b => {
            const tr = document.createElement('tr');
            const fillBadge = b.fillPercent >= 80 ? 'badge-danger' : (b.fillPercent >= 50 ? 'badge-warning' : 'badge-success');

            tr.innerHTML = `
                <td><strong>${b.binCode}</strong></td>
                <td>${b.zoneName || 'Zone ' + b.zoneId}</td>
                <td>${b.locationDesc || '-'}</td>
                <td>${b.capacityLitres} L</td>
                <td>
                    <span class="badge ${fillBadge}">${b.fillPercent}%</span>
                    ${b.dueForCollection ? '<span class="badge badge-danger" style="margin-left: 0.25rem;">DUE</span>' : ''}
                </td>
                <td>${b.lastCollectedAt ? b.lastCollectedAt.replace('T', ' ').substring(0, 16) : 'Never'}</td>
                <td>
                    <div style="display: flex; gap: 0.25rem; align-items: center;">
                        <input type="number" min="0" max="100" id="bin-fill-${b.id}" class="form-control" style="width: 70px;" value="${b.fillPercent}">
                        <button class="btn btn-secondary btn-sm" onclick="updateBinFill(${b.id})">Set</button>
                        <button class="btn btn-success btn-sm" onclick="collectBin(${b.id})">Collect</button>
                    </div>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading waste bins:', err);
    }
}

async function updateBinFill(id) {
    const input = document.getElementById(`bin-fill-${id}`);
    const percent = parseInt(input.value, 10);
    if (isNaN(percent) || percent < 0 || percent > 100) {
        alert('Please enter a percentage between 0 and 100');
        return;
    }

    try {
        await apiFetch(`waste/bins/${id}/fill`, {
            method: 'PUT',
            body: { fillPercent: percent }
        });
        loadWasteBins();
    } catch (err) {
        alert('Update failed: ' + err.message);
    }
}

async function collectBin(id) {
    try {
        await apiFetch(`waste/bins/${id}/collect`, { method: 'POST' });
        loadWasteBins();
    } catch (err) {
        alert('Collection failed: ' + err.message);
    }
}
