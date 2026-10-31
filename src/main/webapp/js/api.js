/**
 * Global HTTP client wrapping fetch with Bearer token authentication and 401 redirection.
 */
async function apiFetch(path, options = {}) {
    const token = sessionStorage.getItem('token');
    const headers = options.headers ? { ...options.headers } : {};

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    if (options.body && typeof options.body === 'object' && !(options.body instanceof FormData)) {
        options.body = JSON.stringify(options.body);
        if (!headers['Content-Type']) {
            headers['Content-Type'] = 'application/json';
        }
    }

    const cleanPath = path.startsWith('/') ? path.substring(1) : path;
    const url = cleanPath.startsWith('api/') ? cleanPath : `api/${cleanPath}`;

    const response = await fetch(url, { ...options, headers });

    if (response.status === 401) {
        sessionStorage.clear();
        window.location.href = 'login.html';
        throw new Error('Session expired or unauthorized');
    }

    const contentType = response.headers.get('content-type');
    const isJson = contentType && contentType.includes('application/json');

    if (!response.ok) {
        let errorMsg = `HTTP Error ${response.status}`;
        if (isJson) {
            const errObj = await response.json();
            errorMsg = errObj.message || errObj.error || errorMsg;
        } else {
            const text = await response.text();
            if (text) errorMsg = text;
        }
        throw new Error(errorMsg);
    }

    return isJson ? response.json() : response.text();
}
