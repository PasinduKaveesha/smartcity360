/**
 * Sensor management UI controller.
 */
async function loadSensors() {
    try {
        const sensors = await apiFetch('sensors');
        const tbody = document.getElementById('sensors-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        sensors.forEach(s => {
            const tr = document.createElement('tr');
            const unit = s.sensorType === 'NOISE' ? 'dB' : (s.sensorType === 'PARKING' ? '%' : '°C');
            const valDisplay = s.latestValue !== null ? `${s.latestValue.toFixed(1)} ${unit}` : 'No readings';

            tr.innerHTML = `
                <td><strong>${s.sensorCode}</strong></td>
                <td><span class="badge badge-info">${s.sensorType}</span></td>
                <td>${s.zoneName || 'Zone ' + s.zoneId}</td>
                <td><span style="font-size: 1.1rem; font-weight: 700; color: #60a5fa;">${valDisplay}</span></td>
                <td><span class="badge ${s.active ? 'badge-success' : 'badge-danger'}">${s.active ? 'ACTIVE' : 'OFFLINE'}</span></td>
                <td>
                    <div style="display: flex; gap: 0.25rem;">
                        <input type="number" step="0.1" id="sensor-val-${s.id}" class="form-control" style="width: 80px;" placeholder="Val">
                        <button class="btn btn-primary btn-sm" onclick="sendSensorReading(${s.id})">Push</button>
                    </div>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading sensors:', err);
    }
}

async function sendSensorReading(sensorId) {
    const input = document.getElementById(`sensor-val-${sensorId}`);
    const val = parseFloat(input.value);
    if (isNaN(val)) {
        alert('Please enter a valid numeric value');
        return;
    }

    try {
        await apiFetch(`sensors/${sensorId}/readings`, {
            method: 'POST',
            body: { value: val }
        });
        input.value = '';
        loadSensors();
    } catch (err) {
        alert('Push reading failed: ' + err.message);
    }
}
