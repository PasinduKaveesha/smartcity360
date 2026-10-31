/**
 * Energy management UI controller demonstrating the Decorator pattern.
 */
let currentMeters = [];

async function loadEnergyMeters() {
    try {
        currentMeters = await apiFetch('energy/meters');
        const select = document.getElementById('energy-meter-select');
        const tbody = document.getElementById('energy-meters-table-body');
        if (select) select.innerHTML = '<option value="">-- Choose Meter --</option>';
        if (tbody) tbody.innerHTML = '';

        currentMeters.forEach(m => {
            if (select) {
                const opt = document.createElement('option');
                opt.value = m.id;
                opt.textContent = `${m.meterCode} (${m.zoneName})`;
                select.appendChild(opt);
            }
            if (tbody) {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td><strong>${m.meterCode}</strong></td>
                    <td>${m.zoneName}</td>
                    <td><span class="badge badge-info">${m.layers || 'None'}</span></td>
                    <td>${m.installedAt ? m.installedAt.replace('T', ' ').substring(0, 19) : '-'}</td>
                `;
                tbody.appendChild(tr);
            }
        });
    } catch (err) {
        console.error('Failed loading meters:', err);
    }
}

function getSelectedLayers() {
    const layers = [];
    if (document.getElementById('layer-encryption')?.checked) layers.push('ENCRYPTION');
    if (document.getElementById('layer-authentication')?.checked) layers.push('AUTHENTICATION');
    if (document.getElementById('layer-compression')?.checked) layers.push('COMPRESSION');
    return layers;
}

async function previewEnergyStream() {
    const readingVal = document.getElementById('energy-reading-input').value.trim() || '150.5';
    const layers = getSelectedLayers();
    const resultBox = document.getElementById('energy-preview-result');

    try {
        const res = await apiFetch('energy/preview', {
            method: 'POST',
            body: { reading: readingVal, layers: layers }
        });

        resultBox.innerHTML = `
            <div style="font-weight: 600; color: #60a5fa; margin-bottom: 0.5rem;">Decorator Pipeline: ${res.description}</div>
            <div>Raw Size: <strong>${res.rawSize} bytes</strong> | Secured Size: <strong>${res.securedSize} bytes</strong></div>
            <div>Round-trip Validation: <span class="badge ${res.roundTripMatches ? 'badge-success' : 'badge-danger'}">
                ${res.roundTripMatches ? 'SUCCESS (MATCH)' : 'FAILED'}
            </span></div>
        `;
        resultBox.style.display = 'block';
    } catch (err) {
        alert('Preview failed: ' + err.message);
    }
}

async function sendEnergyReading() {
    const meterId = document.getElementById('energy-meter-select').value;
    if (!meterId) {
        alert('Please select an energy meter first');
        return;
    }
    const readingVal = parseFloat(document.getElementById('energy-reading-input').value);
    if (isNaN(readingVal)) {
        alert('Please enter a valid numeric kWh reading');
        return;
    }

    try {
        const res = await apiFetch(`energy/meters/${meterId}/readings`, {
            method: 'POST',
            body: { readingKwh: readingVal }
        });

        alert(`Reading saved successfully! Raw: ${res.rawSize}B, Secured: ${res.securedSize}B`);
        loadMeterReadings(meterId);
    } catch (err) {
        alert('Submission failed: ' + err.message);
    }
}

async function loadMeterReadings(meterId) {
    if (!meterId) return;
    try {
        const readings = await apiFetch(`energy/meters/${meterId}/readings`);
        const tbody = document.getElementById('energy-readings-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';
        readings.forEach(r => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${r.id}</td>
                <td><strong>${r.readingKwh.toFixed(2)} kWh</strong></td>
                <td>${r.rawSize} B</td>
                <td>${r.securedSize} B</td>
                <td><span class="badge badge-info">${r.layersApplied || 'None'}</span></td>
                <td>${r.recordedAt ? r.recordedAt.replace('T', ' ').substring(0, 19) : '-'}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading readings:', err);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btn-energy-preview')?.addEventListener('click', previewEnergyStream);
    document.getElementById('btn-energy-send')?.addEventListener('click', sendEnergyReading);
    document.getElementById('energy-meter-select')?.addEventListener('change', (e) => {
        loadMeterReadings(e.target.value);
    });
});
