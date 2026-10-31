/**
 * Policy management UI controller demonstrating the Interpreter pattern.
 */
async function loadPolicies() {
    try {
        const policies = await apiFetch('policies');
        const tbody = document.getElementById('policies-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        policies.forEach(p => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong>${p.name}</strong></td>
                <td><code>${p.ruleText}</code></td>
                <td><span class="badge badge-warning">${p.actionText}</span></td>
                <td><span class="badge ${p.enabled ? 'badge-success' : 'badge-danger'}">${p.enabled ? 'ENABLED' : 'DISABLED'}</span></td>
                <td>
                    <button class="btn btn-secondary btn-sm" onclick="editPolicy(${p.id}, '${escapeQuotes(p.name)}', '${escapeQuotes(p.ruleText)}', '${escapeQuotes(p.actionText)}', ${p.enabled})">Edit</button>
                    <button class="btn btn-danger btn-sm admin-only" onclick="deletePolicy(${p.id})">Del</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading policies:', err);
    }
}

function escapeQuotes(str) {
    return (str || '').replace(/'/g, "\\'").replace(/"/g, '&quot;');
}

function editPolicy(id, name, ruleText, actionText, enabled) {
    document.getElementById('policy-id').value = id;
    document.getElementById('policy-name').value = name;
    document.getElementById('policy-rule').value = ruleText;
    document.getElementById('policy-action').value = actionText;
    document.getElementById('policy-enabled').checked = enabled;
}

async function validatePolicySyntax() {
    const ruleText = document.getElementById('policy-rule').value.trim();
    const resultBox = document.getElementById('policy-validation-box');

    try {
        const res = await apiFetch('policies/validate', {
            method: 'POST',
            body: { ruleText }
        });

        if (res.valid) {
            resultBox.className = 'alert alert-success';
            resultBox.textContent = 'Rule syntax is 100% valid!';
        } else {
            resultBox.className = 'alert alert-danger';
            resultBox.textContent = `Syntax Error at position ${res.errorPosition}: ${res.message}`;
        }
        resultBox.style.display = 'block';
    } catch (err) {
        resultBox.className = 'alert alert-danger';
        resultBox.textContent = err.message;
        resultBox.style.display = 'block';
    }
}

async function savePolicy(e) {
    e.preventDefault();
    const id = document.getElementById('policy-id').value;
    const name = document.getElementById('policy-name').value.trim();
    const ruleText = document.getElementById('policy-rule').value.trim();
    const actionText = document.getElementById('policy-action').value.trim();
    const enabled = document.getElementById('policy-enabled').checked;

    try {
        if (id) {
            await apiFetch(`policies/${id}`, {
                method: 'PUT',
                body: { name, ruleText, actionText, enabled }
            });
            alert('Policy updated successfully');
        } else {
            await apiFetch('policies', {
                method: 'POST',
                body: { name, ruleText, actionText, enabled }
            });
            alert('Policy created successfully');
        }
        document.getElementById('policy-form').reset();
        document.getElementById('policy-id').value = '';
        document.getElementById('policy-validation-box').style.display = 'none';
        loadPolicies();
    } catch (err) {
        alert('Saving policy failed: ' + err.message);
    }
}

async function deletePolicy(id) {
    if (!confirm('Are you sure you want to delete this policy?')) return;
    try {
        await apiFetch(`policies/${id}`, { method: 'DELETE' });
        loadPolicies();
    } catch (err) {
        alert('Delete failed: ' + err.message);
    }
}

async function evaluateAllPolicies() {
    try {
        const res = await apiFetch('policies/evaluate-all', { method: 'POST' });
        alert(`Evaluation completed! Evaluated: ${res.policiesEvaluated}, Triggered: ${res.policiesTriggered}\nContext: ${res.contextSnapshot}`);
        loadPolicyLogs();
    } catch (err) {
        alert('Evaluation failed: ' + err.message);
    }
}

async function loadPolicyLogs() {
    try {
        const logs = await apiFetch('policies/logs?limit=25');
        const tbody = document.getElementById('policy-logs-table-body');
        if (!tbody) return;
        tbody.innerHTML = '';

        logs.forEach(l => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${l.id}</td>
                <td><strong>${l.policyName}</strong></td>
                <td><span class="badge ${l.triggered ? 'badge-danger' : 'badge-success'}">${l.triggered ? 'TRIGGERED' : 'PASSED'}</span></td>
                <td>${l.actionText || '-'}</td>
                <td><small style="color: #94a3b8;">${l.contextSnapshot}</small></td>
                <td>${l.evaluatedAt ? l.evaluatedAt.replace('T', ' ').substring(0, 19) : '-'}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error('Failed loading logs:', err);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btn-validate-policy')?.addEventListener('click', validatePolicySyntax);
    document.getElementById('policy-form')?.addEventListener('submit', savePolicy);
    document.getElementById('btn-evaluate-all')?.addEventListener('click', evaluateAllPolicies);
});
