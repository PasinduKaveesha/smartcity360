/**
 * Residents and staff directory UI controller.
 */
async function loadResidents() {
    try {
        const residents = await apiFetch('residents');
        const tbody = document.getElementById('residents-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        residents.forEach(r => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${r.id}</td>
                <td><strong>${r.fullName}</strong></td>
                <td>${r.email}</td>
                <td>${r.address || '-'}</td>
                <td>${r.zoneName || (r.zoneId ? 'Zone ' + r.zoneId : '-')}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading residents:', err);
    }
}

async function loadStaff() {
    try {
        const staff = await apiFetch('staff');
        const tbody = document.getElementById('staff-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        staff.forEach(s => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${s.id}</td>
                <td><strong>${s.fullName}</strong></td>
                <td><span class="badge badge-info">${s.role}</span></td>
                <td>${s.email}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading staff:', err);
    }
}

async function registerResident(e) {
    e.preventDefault();
    const fullName = document.getElementById('resident-name').value.trim();
    const email = document.getElementById('resident-email').value.trim();
    const address = document.getElementById('resident-address').value.trim();
    const zoneId = parseInt(document.getElementById('resident-zone-id').value, 10);

    try {
        await apiFetch('residents', {
            method: 'POST',
            body: { fullName, email, address, zoneId: isNaN(zoneId) ? null : zoneId }
        });
        alert('Resident registered successfully');
        document.getElementById('resident-form').reset();
        loadResidents();
    } catch (err) {
        alert('Registration failed: ' + err.message);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('resident-form')?.addEventListener('submit', registerResident);
});
