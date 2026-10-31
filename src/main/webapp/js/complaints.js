/**
 * Complaints management UI controller demonstrating the Chain of Responsibility pattern.
 */
let allComplaints = [];

async function loadComplaints() {
    try {
        allComplaints = await apiFetch('complaints');
        const tbody = document.getElementById('complaints-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        allComplaints.forEach(c => {
            const tr = document.createElement('tr');
            tr.style.cursor = 'pointer';
            tr.onclick = () => showComplaintTrace(c.id);

            const badgeClass = c.status === 'RESOLVED' ? 'badge-success' : (c.status === 'OPEN' ? 'badge-warning' : 'badge-danger');
            tr.innerHTML = `
                <td>#${c.id}</td>
                <td><strong>${c.title}</strong></td>
                <td>${c.residentName || 'Resident #' + c.residentId}</td>
                <td><span class="badge badge-info">${c.severity}</span></td>
                <td><span class="badge ${badgeClass}">${c.status}</span></td>
                <td>${c.resolvedByStaffName || '-'}</td>
                <td>${c.createdAt ? c.createdAt.replace('T', ' ').substring(0, 16) : '-'}</td>
                <td><button class="btn btn-secondary btn-sm" onclick="event.stopPropagation(); showComplaintTrace(${c.id})">Trace</button></td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading complaints:', err);
    }
}

async function showComplaintTrace(id) {
    try {
        const c = await apiFetch(`complaints/${id}`);
        const modal = document.getElementById('complaint-trace-modal');
        const titleEl = document.getElementById('trace-complaint-title');
        const timeline = document.getElementById('trace-timeline');

        if (!modal || !timeline) return;

        titleEl.textContent = `Complaint #${c.id}: ${c.title} (${c.severity}) - Status: ${c.status}`;
        timeline.innerHTML = '';

        if (!c.traces || c.traces.length === 0) {
            timeline.innerHTML = '<div style="color: #94a3b8;">No trace recorded.</div>';
        } else {
            c.traces.forEach(t => {
                const item = document.createElement('div');
                item.className = 'timeline-item';
                item.innerHTML = `
                    <div class="timeline-time">${t.loggedAt ? t.loggedAt.replace('T', ' ').substring(0, 19) : ''}</div>
                    <div class="timeline-content">
                        <strong>Step ${t.stepNo}: ${t.handlerRole} (${t.handlerName})</strong>
                        <span class="badge ${t.action === 'HANDLED' ? 'badge-success' : (t.action === 'ESCALATED' ? 'badge-warning' : 'badge-danger')}" style="margin-left: 0.5rem;">
                            ${t.action}
                        </span>
                        <div style="font-size: 0.8rem; color: #cbd5e1; margin-top: 0.25rem;">${t.note || ''}</div>
                    </div>
                `;
                timeline.appendChild(item);
            });
        }

        modal.style.display = 'block';
    } catch (err) {
        alert('Failed loading trace: ' + err.message);
    }
}

async function submitComplaint(e) {
    e.preventDefault();
    const residentId = document.getElementById('complaint-resident-id').value;
    const title = document.getElementById('complaint-title').value.trim();
    const severity = document.getElementById('complaint-severity').value;
    const description = document.getElementById('complaint-description').value.trim();

    try {
        const res = await apiFetch('complaints', {
            method: 'POST',
            body: { residentId: parseInt(residentId, 10), title, severity, description }
        });

        alert(`Complaint #${res.id} submitted! Outcome: ${res.status} by ${res.resolvedByStaffName || 'None'}`);
        document.getElementById('complaint-form').reset();
        loadComplaints();
        showComplaintTrace(res.id);
    } catch (err) {
        alert('Submission failed: ' + err.message);
    }
}

async function loadEscalationChain() {
    try {
        const res = await apiFetch('complaints/chain');
        const container = document.getElementById('chain-order-display');
        if (container) {
            container.innerHTML = res.order.map((r, i) => `<span class="badge badge-info">${i + 1}. ${r}</span>`).join(' ➔ ');
        }
    } catch (err) {
        console.error('Failed loading escalation chain:', err);
    }
}

async function updateEscalationChain(newOrder) {
    try {
        await apiFetch('complaints/chain', {
            method: 'PUT',
            body: { order: newOrder }
        });
        alert('Escalation chain sequence updated successfully');
        loadEscalationChain();
    } catch (err) {
        alert('Failed updating chain: ' + err.message);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('complaint-form')?.addEventListener('submit', submitComplaint);
    document.getElementById('btn-reorder-reverse')?.addEventListener('click', () => {
        updateEscalationChain(['CITY_MANAGER', 'SUPERVISOR', 'LOCAL_STAFF']);
    });
    document.getElementById('btn-reorder-default')?.addEventListener('click', () => {
        updateEscalationChain(['LOCAL_STAFF', 'SUPERVISOR', 'CITY_MANAGER']);
    });
});
