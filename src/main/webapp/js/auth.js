/**
 * Authentication and role-based UI control visibility manager.
 */
document.addEventListener('DOMContentLoaded', () => {
    const token = sessionStorage.getItem('token');
    const role = sessionStorage.getItem('role');
    const fullName = sessionStorage.getItem('fullName');

    if (!token && !window.location.pathname.endsWith('login.html')) {
        window.location.href = 'login.html';
        return;
    }

    const userNameEl = document.getElementById('current-user-name');
    const userRoleEl = document.getElementById('current-user-role');
    if (userNameEl && fullName) userNameEl.textContent = fullName;
    if (userRoleEl && role) userRoleEl.textContent = role;

    // Apply role-based visibility rules
    if (role !== 'ADMIN') {
        document.querySelectorAll('.admin-only').forEach(el => {
            el.style.display = 'none';
        });
    }

    if (role === 'RESIDENT') {
        document.querySelectorAll('.staff-only').forEach(el => {
            el.style.display = 'none';
        });
    }

    const logoutBtn = document.getElementById('btn-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', async () => {
            try {
                await apiFetch('auth/logout', { method: 'POST' });
            } catch (err) {
                console.warn('Logout error:', err);
            } finally {
                sessionStorage.clear();
                window.location.href = 'login.html';
            }
        });
    }
});
