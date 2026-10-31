/**
 * Zone management UI controller demonstrating the Composite pattern.
 */
async function loadZoneTree() {
    const container = document.getElementById('zone-tree-container');
    if (!container) return;
    try {
        const tree = await apiFetch('zones/tree');
        container.innerHTML = '';
        tree.forEach(node => {
            container.appendChild(renderZoneNode(node));
        });
    } catch (err) {
        container.innerHTML = `<div class="alert alert-danger">Failed to load zone tree: ${err.message}</div>`;
    }
}

function renderZoneNode(node) {
    const wrapper = document.createElement('div');
    wrapper.className = 'tree-node';

    const header = document.createElement('div');
    header.className = 'tree-header';

    const title = document.createElement('div');
    title.className = 'tree-title';
    title.innerHTML = `
        <strong>${node.name}</strong> 
        <span class="badge ${node.level === 'BUILDING' ? 'badge-warning' : 'badge-info'}">${node.level}</span>
        <span class="badge ${node.powered ? 'badge-success' : 'badge-danger'}">${node.powered ? 'POWER ON' : 'SHUTDOWN'}</span>
        <span style="color: #94a3b8; font-size: 0.8rem;">${node.energyUsageKwh.toFixed(2)} kWh</span>
    `;

    const actions = document.createElement('div');
    actions.style.display = 'flex';
    actions.style.gap = '0.35rem';

    const role = sessionStorage.getItem('role');
    if (role === 'ADMIN') {
        const btnShutdown = document.createElement('button');
        btnShutdown.className = 'btn btn-danger btn-sm';
        btnShutdown.textContent = 'Shutdown';
        btnShutdown.onclick = () => shutdownZone(node.id);

        const btnRestore = document.createElement('button');
        btnRestore.className = 'btn btn-success btn-sm';
        btnRestore.textContent = 'Restore';
        btnRestore.onclick = () => restoreZone(node.id);

        actions.appendChild(btnShutdown);
        actions.appendChild(btnRestore);
    }

    const btnReport = document.createElement('button');
    btnReport.className = 'btn btn-secondary btn-sm';
    btnReport.textContent = 'Report';
    btnReport.onclick = () => showZoneReport(node.id);
    actions.appendChild(btnReport);

    header.appendChild(title);
    header.appendChild(actions);
    wrapper.appendChild(header);

    if (node.children && node.children.length > 0) {
        const childrenContainer = document.createElement('div');
        node.children.forEach(child => {
            childrenContainer.appendChild(renderZoneNode(child));
        });
        wrapper.appendChild(childrenContainer);
    }

    return wrapper;
}

async function shutdownZone(id) {
    try {
        await apiFetch(`zones/${id}/shutdown`, { method: 'POST' });
        loadZoneTree();
    } catch (err) {
        alert('Shutdown failed: ' + err.message);
    }
}

async function restoreZone(id) {
    try {
        await apiFetch(`zones/${id}/restore`, { method: 'POST' });
        loadZoneTree();
    } catch (err) {
        alert('Restore failed: ' + err.message);
    }
}

async function showZoneReport(id) {
    try {
        const res = await apiFetch(`zones/${id}/report`);
        const reportBox = document.getElementById('zone-report-modal');
        const reportContent = document.getElementById('zone-report-content');
        if (reportBox && reportContent) {
            reportContent.textContent = res.report;
            reportBox.style.display = 'block';
        } else {
            alert(res.report);
        }
    } catch (err) {
        alert('Report generation failed: ' + err.message);
    }
}
