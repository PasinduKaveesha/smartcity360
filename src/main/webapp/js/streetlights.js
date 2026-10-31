/**
 * Streetlight management UI controller demonstrating the Flyweight pattern.
 */
let currentStreetlightPage = 1;

async function loadStreetlightStats() {
    try {
        const stats = await apiFetch('streetlights/stats');
        document.getElementById('stat-total-lights').textContent = stats.totalLights.toLocaleString();
        document.getElementById('stat-unique-models').textContent = stats.uniqueModelsInRegistry.toLocaleString();
        document.getElementById('stat-shared-memory').textContent = `${(stats.sharedMemoryBytes / 1024).toFixed(2)} KB`;
        document.getElementById('stat-unshared-memory').textContent = `${(stats.unsharedMemoryBytes / 1024).toFixed(2)} KB`;
        document.getElementById('stat-savings-percent').textContent = `${stats.savingsPercent}%`;
    } catch (err) {
        console.error('Failed loading streetlight stats:', err);
    }
}

async function loadStreetlights(page = 1) {
    currentStreetlightPage = page;
    try {
        const lights = await apiFetch(`streetlights?page=${page}&size=15`);
        const tbody = document.getElementById('streetlights-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        lights.forEach(l => {
            const tr = document.createElement('tr');
            const stateBadge = l.state === 'ON' ? 'badge-success' : (l.state === 'DIMMED' ? 'badge-warning' : 'badge-danger');
            tr.innerHTML = `
                <td>#${l.id}</td>
                <td><strong>${l.modelCode}</strong> (${l.wattage}W, ${l.maxLumens}lm)</td>
                <td>${l.zoneName || 'Zone ' + l.zoneId}</td>
                <td><span class="badge ${stateBadge}">${l.state}</span></td>
                <td>${l.brightnessPercent}%</td>
                <td>
                    <button class="btn btn-success btn-sm" onclick="setLampState(${l.id}, 'ON', 100)">ON</button>
                    <button class="btn btn-warning btn-sm" onclick="setLampState(${l.id}, 'DIMMED', 50)">50%</button>
                    <button class="btn btn-danger btn-sm" onclick="setLampState(${l.id}, 'OFF', 0)">OFF</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

        document.getElementById('streetlights-page-indicator').textContent = `Page ${page}`;
    } catch (err) {
        console.error('Failed loading streetlights:', err);
    }
}

async function setLampState(id, state, brightness) {
    try {
        await apiFetch(`streetlights/${id}/state`, {
            method: 'PUT',
            body: { state, brightnessPercent: brightness }
        });
        loadStreetlights(currentStreetlightPage);
    } catch (err) {
        alert('State change failed: ' + err.message);
    }
}

async function bulkGenerateStreetlights() {
    const count = parseInt(prompt('How many streetlights to bulk-generate (max 20000)?', '200'), 10);
    if (!count || isNaN(count) || count <= 0) return;

    try {
        await apiFetch(`streetlights/bulk?count=${count}`, { method: 'POST' });
        alert(`Successfully generated ${count} streetlights!`);
        loadStreetlightStats();
        loadStreetlights(1);
    } catch (err) {
        alert('Bulk generation failed: ' + err.message);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btn-bulk-streetlights')?.addEventListener('click', bulkGenerateStreetlights);
    document.getElementById('btn-prev-streetlights')?.addEventListener('click', () => {
        if (currentStreetlightPage > 1) loadStreetlights(currentStreetlightPage - 1);
    });
    document.getElementById('btn-next-streetlights')?.addEventListener('click', () => {
        loadStreetlights(currentStreetlightPage + 1);
    });
});
