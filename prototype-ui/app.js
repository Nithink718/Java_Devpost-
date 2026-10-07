// DEVKIT Core Engine

const defaultDB = {
    workspaces: [
        { id: 'ws1', name: 'Default Workspace', envs: [{ key: 'API_URL', value: 'http://localhost:8080' }] }
    ],
    activeWs: 'ws1',
    apis: [],
    snippets: [],
    notes: [],
    history: []
};

let DB = JSON.parse(localStorage.getItem('devkit_db')) || defaultDB;
if(!DB.workspaces) DB = defaultDB; // Migration safety

function saveDB() {
    localStorage.setItem('devkit_db', JSON.stringify(DB));
}

function showToast(msg, type='info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `<i data-lucide="${type==='error'?'alert-circle':'check-circle'}" style="color:var(--${type==='error'?'error':'gold-primary'})"></i> ${msg}`;
    container.appendChild(toast);
    lucide.createIcons();
    setTimeout(() => { toast.style.opacity = '0'; setTimeout(()=>toast.remove(), 200); }, 3000);
}
// --- LANDING PAGE ---
function enterApp() {
    const lp = document.getElementById('landing-page');
    if(!lp) return;
    lp.style.opacity = '0';
    lp.style.transform = 'scale(1.05)';
    setTimeout(() => {
        lp.style.display = 'none';
    }, 500);
}

// --- NAVIGATION ---
function nav(viewId) {
    document.querySelectorAll('.view').forEach(v => v.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(v => v.classList.remove('active'));
    
    const target = document.getElementById('view-' + viewId);
    if(target) target.classList.add('active');
    
    const navItem = document.querySelector(`.nav-item[data-view="${viewId}"]`);
    if(navItem) navItem.classList.add('active');
    
    // View specific initializations
    if(viewId === 'api') renderApis();
    if(viewId === 'env') renderEnvs();
    if(viewId === 'snippets') renderSnippets();
    if(viewId === 'history') renderHistory();
}

function filterTools(category, element) {
    document.querySelectorAll('.filter-pill').forEach(p => p.classList.remove('active'));
    element.classList.add('active');
    
    const cards = document.querySelectorAll('.grid .card');
    cards.forEach(card => {
        if (category === 'ALL') {
            card.style.display = '';
        } else {
            const badge = card.querySelector('.card-badge');
            if (badge && badge.textContent.trim().toUpperCase() === category) {
                card.style.display = '';
            } else {
                card.style.display = 'none';
            }
        }
    });
}

// --- CMD PALETTE ---
const commands = [
    { name: 'Dashboard Overview', icon: 'layout-dashboard', view: 'dashboard', cat: 'WORKSPACE' },
    { name: 'API Collections', icon: 'globe', view: 'api', cat: 'WORKSPACE' },
    { name: 'Environments', icon: 'server', view: 'env', cat: 'WORKSPACE' },
    { name: 'Snippets & Notes', icon: 'code-2', view: 'snippets', cat: 'WORKSPACE' },
    { name: 'Activity History', icon: 'history', view: 'history', cat: 'WORKSPACE' },
    { name: 'JSON Formatter', icon: 'brackets', view: 'json', cat: 'TOOLS' },
    { name: 'Encoding & Tokens', icon: 'key', view: 'tokens', cat: 'SECURITY', keywords: 'jwt token decoder base64 encode decode' },
    { name: 'Hash Generator', icon: 'hash', view: 'hash', cat: 'SECURITY' },
    { name: 'Diff Checker', icon: 'git-compare', view: 'diff', cat: 'TEXT' },
    { name: 'Error Analyzer', icon: 'alert-triangle', view: 'error', cat: 'DEBUG' },
    { name: 'Cron Builder', icon: 'clock', view: 'cron', cat: 'SERVER' },
    { name: 'Webhook Tester', icon: 'radio-receiver', view: 'webhook', cat: 'NETWORK' },
    { name: 'Settings & Export', icon: 'settings', view: 'settings', cat: 'SYSTEM' }
];

let cmdIndex = 0;
const cmdInput = document.getElementById('cmd-input');
const cmdOverlay = document.getElementById('cmd-overlay');
const cmdResults = document.getElementById('cmd-results');

function openCmd() { cmdOverlay.classList.add('active'); cmdInput.value = ''; renderCmd(''); cmdInput.focus(); }
function closeCmd() { cmdOverlay.classList.remove('active'); }

function renderCmd(query) {
    const q = query.toLowerCase();
    const filtered = commands.filter(c => c.name.toLowerCase().includes(q) || c.cat.toLowerCase().includes(q) || (c.keywords && c.keywords.toLowerCase().includes(q)));
    cmdResults.innerHTML = '';
    
    if(filtered.length === 0) {
        cmdResults.innerHTML = `<div style="padding:1rem; color:var(--text-muted); text-align:center;">No tools found matching "${query}"</div>`;
        return;
    }
    
    let lastCat = '';
    filtered.forEach((c, idx) => {
        if(c.cat !== lastCat) {
            cmdResults.innerHTML += `<div class="cmd-category">${c.cat}</div>`;
            lastCat = c.cat;
        }
        cmdResults.innerHTML += `
        <div class="cmd-item ${idx === cmdIndex ? 'active' : ''}" onclick="nav('${c.view}'); closeCmd();">
            <div class="cmd-item-left"><i data-lucide="${c.icon}"></i> ${c.name}</div>
            <div style="font-family:var(--font-mono); font-size:0.7rem;">Enter</div>
        </div>`;
    });
    lucide.createIcons();
}

cmdInput.addEventListener('keydown', (e) => {
    const q = cmdInput.value.toLowerCase();
    const filtered = commands.filter(c => c.name.toLowerCase().includes(q) || c.cat.toLowerCase().includes(q) || (c.keywords && c.keywords.toLowerCase().includes(q)));
    
    if(e.key === 'Escape') closeCmd();
    else if(e.key === 'ArrowDown') { e.preventDefault(); cmdIndex = Math.min(cmdIndex + 1, filtered.length - 1); renderCmd(cmdInput.value); }
    else if(e.key === 'ArrowUp') { e.preventDefault(); cmdIndex = Math.max(cmdIndex - 1, 0); renderCmd(cmdInput.value); }
    else if(e.key === 'Enter' && filtered.length > 0) {
        e.preventDefault();
        nav(filtered[cmdIndex].view);
        closeCmd();
    }
});

document.addEventListener('keydown', e => {
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') { e.preventDefault(); openCmd(); }
});

// --- API WORKSPACE ---
function renderApis() {
    const list = document.getElementById('api-list');
    list.innerHTML = `<div class="ide-tabbar"><div class="ide-tab active">Saved Requests</div></div>`;
    
    const wsApis = DB.apis.filter(a => a.ws === DB.activeWs);
    if(wsApis.length === 0) list.innerHTML += `<div class="p-4 text-muted" style="text-align:center; font-size:0.85rem;">No saved APIs</div>`;
    
    wsApis.forEach(a => {
        list.innerHTML += `
        <div class="list-item" onclick="loadApi('${a.id}')">
            <div class="list-item-title">${a.name}</div>
            <div class="list-item-meta"><span class="badge ${a.method.toLowerCase()}">${a.method}</span> <span>${a.url.substring(0,20)}...</span></div>
        </div>`;
    });
}
function createApiReq() {
    const api = { id: 'api'+Date.now(), ws: DB.activeWs, name: 'New Request', method: 'GET', url: 'https://jsonplaceholder.typicode.com/todos/1', headers: '{}' };
    DB.apis.push(api); saveDB(); renderApis(); loadApi(api.id);
}
function loadApi(id) {
    const api = DB.apis.find(x => x.id === id);
    if(!api) return;
    document.getElementById('api-method').value = api.method;
    document.getElementById('api-url').value = api.url;
    document.getElementById('api-headers').value = api.headers || '{}';
}
function saveApi() { showToast('API Request Saved'); }
async function sendApi() {
    const url = document.getElementById('api-url').value;
    const method = document.getElementById('api-method').value;
    const out = document.getElementById('api-response');
    out.value = "Sending request...\n";
    try {
        const start = Date.now();
        const res = await fetch(url, { method });
        const time = Date.now() - start;
        const text = await res.text();
        let formatted = text;
        try { formatted = JSON.stringify(JSON.parse(text), null, 2); } catch(e){}
        out.value = `HTTP ${res.status} ${res.statusText} (${time}ms)\n\n${formatted}`;
        logHistory(`API Request: ${method} ${url}`);
    } catch(e) {
        out.value = `NETWORK ERROR\n\nFailed to fetch. This may be due to CORS restrictions or an invalid URL.\n\nDetails: ${e.message}`;
    }
}

// --- ERROR ANALYZER ---
function analyzeError() {
    const input = document.getElementById('error-input').value;
    const out = document.getElementById('error-output');
    if(!input) return;
    
    let html = `<div class="p-4" style="background:#fff; border-radius:12px;">`;
    if(input.includes('NullPointerException')) {
        html += `<h4><span class="error-highlight">NullPointerException</span></h4>
                 <p class="mt-4" style="color:var(--text-muted); font-size:0.95rem;">Likely Cause: You are attempting to call a method or access a property on an object reference that is null.</p>
                 <div class="code-block mt-4" style="color:var(--success);">Fix: Add a null check (e.g. if(obj != null)) before accessing the reference.</div>`;
    } else if (input.includes('ArrayIndexOutOfBoundsException')) {
        html += `<h4><span class="error-highlight">ArrayIndexOutOfBoundsException</span></h4>
                 <p class="mt-4" style="color:var(--text-muted); font-size:0.95rem;">Likely Cause: You are trying to access an array index that is negative or greater than/equal to the array size.</p>`;
    } else {
        html += `<h4><span class="error-highlight">Unknown Error Type</span></h4><p class="mt-4">Could not deterministically classify this error.</p>`;
    }
    
    html += `<h4 class="mt-4" style="margin-bottom:0.5rem; font-size:0.85rem; color:var(--text-muted);">PARSED STACK TRACE</h4>`;
    const lines = input.split('\n');
    lines.forEach(l => {
        if(l.trim().startsWith('at ')) {
            const parts = l.trim().substring(3).split('(');
            const method = parts[0];
            const file = parts.length > 1 ? parts[1].replace(')','') : '';
            html += `<div class="stack-frame"><span style="color:var(--text-dark);">${method}</span> <span class="stack-file">(${file})</span></div>`;
        }
    });
    
    html += `</div>`;
    out.innerHTML = html;
    logHistory('Analyzed Stack Trace');
}

// --- CRON BUILDER ---
function setCron(val) { document.getElementById('cron-input').value = val; parseCron(); }
function parseCron() {
    const val = document.getElementById('cron-input').value.split(' ');
    document.getElementById('cp-min').textContent = val[0] || '*';
    document.getElementById('cp-hr').textContent = val[1] || '*';
    document.getElementById('cp-day').textContent = val[2] || '*';
    document.getElementById('cp-mon').textContent = val[3] || '*';
    document.getElementById('cp-wk').textContent = val[4] || '*';
    
    // Very naive human readable for prototype
    const v = val.join(' ');
    let h = "Custom schedule";
    if(v === '* * * * *') h = "Every minute";
    if(v === '*/5 * * * *') h = "Every 5 minutes";
    if(v === '0 * * * *') h = "Every hour on the hour";
    if(v === '0 0 * * *') h = "Every day at midnight";
    if(v === '0 0 * * 1-5') h = "Every weekday at midnight";
    document.getElementById('cron-human').textContent = h;
}

// --- ENCODING & TOKENS ---
function switchTokenTab(tab) {
    document.getElementById('tab-btn-jwt').classList.remove('active');
    document.getElementById('tab-btn-b64').classList.remove('active');
    document.getElementById('tool-jwt').style.display = 'none';
    document.getElementById('tool-b64').style.display = 'none';
    
    document.getElementById('tab-btn-' + tab).classList.add('active');
    document.getElementById('tool-' + tab).style.display = 'flex';
}

function decodeJWT() {
    const input = document.getElementById('jwt-in').value.trim();
    const err = document.getElementById('jwt-error');
    const hout = document.getElementById('jwt-header-out');
    const pout = document.getElementById('jwt-payload-out');
    const sout = document.getElementById('jwt-sig-out');
    
    hout.textContent = ''; pout.textContent = ''; sout.textContent = ''; err.style.display = 'none';
    if(!input) return;
    
    const parts = input.split('.');
    if(parts.length !== 3) { err.style.display = 'block'; return; }
    
    try {
        const b64DecodeUnicode = (str) => {
            let base64 = str.replace(/-/g, '+').replace(/_/g, '/');
            while (base64.length % 4 !== 0) base64 += '=';
            const binStr = atob(base64);
            const bytes = new Uint8Array(binStr.length);
            for (let i = 0; i < binStr.length; i++) bytes[i] = binStr.charCodeAt(i);
            return new TextDecoder('utf-8').decode(bytes);
        };
        
        const header = JSON.parse(b64DecodeUnicode(parts[0]));
        const payload = JSON.parse(b64DecodeUnicode(parts[1]));
        
        if(payload.exp) payload.exp_human = new Date(payload.exp * 1000).toLocaleString();
        if(payload.iat) payload.iat_human = new Date(payload.iat * 1000).toLocaleString();
        
        hout.textContent = JSON.stringify(header, null, 2);
        pout.textContent = JSON.stringify(payload, null, 2);
        sout.textContent = parts[2];
        logHistory('Decoded JWT');
    } catch(e) {
        err.style.display = 'block';
    }
}

function loadSampleJWT() {
    const header = btoa(JSON.stringify({alg:"HS256",typ:"JWT"})).replace(/=/g,'');
    const payload = btoa(JSON.stringify({sub:"1234567890",name:"DevKit User",iat:1516239022,role:"admin"})).replace(/=/g,'');
    document.getElementById('jwt-in').value = `${header}.${payload}.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c`;
    decodeJWT();
}

function convertBase64() {
    const mode = document.getElementById('b64-mode').value;
    const input = document.getElementById('b64-in').value;
    const out = document.getElementById('b64-out');
    out.value = '';
    if(!input) return;
    
    try {
        if(mode === 'encode') {
            const bytes = new TextEncoder().encode(input);
            let binStr = "";
            for (let i = 0; i < bytes.length; i++) binStr += String.fromCharCode(bytes[i]);
            out.value = btoa(binStr);
            logHistory('Encoded Base64');
        } else {
            const binStr = atob(input.trim());
            const bytes = new Uint8Array(binStr.length);
            for (let i = 0; i < binStr.length; i++) bytes[i] = binStr.charCodeAt(i);
            out.value = new TextDecoder('utf-8').decode(bytes);
            logHistory('Decoded Base64');
        }
    } catch(e) {
        out.value = 'Error: Invalid input for Base64 conversion.';
    }
}

function swapB64() {
    const modeEl = document.getElementById('b64-mode');
    const inEl = document.getElementById('b64-in');
    const outEl = document.getElementById('b64-out');
    
    const oldOut = outEl.value;
    modeEl.value = modeEl.value === 'encode' ? 'decode' : 'encode';
    
    if(!oldOut.startsWith('Error:')) {
        inEl.value = oldOut;
    }
    convertBase64();
}

function copyText(txt) {
    if(!txt) return;
    navigator.clipboard.writeText(txt).then(() => {
        showToast('Copied to clipboard');
    }).catch(err => {
        showToast('Failed to copy', 'error');
    });
}

// --- WEBHOOK TESTER ---
function fireMockWebhook() {
    const list = document.getElementById('wh-list');
    const id = Date.now();
    const div = document.createElement('div');
    div.className = 'list-item';
    div.innerHTML = `<div class="list-item-title">POST /hook/local-test</div><div class="list-item-meta"><span class="badge post">POST</span> <span>${new Date().toLocaleTimeString()}</span></div>`;
    div.onclick = () => {
        document.querySelectorAll('#wh-list .list-item').forEach(e => e.classList.remove('active'));
        div.classList.add('active');
        document.getElementById('wh-details').innerHTML = `
            <div class="p-4 font-mono" style="font-size:0.9rem;">
                <div style="color:var(--text-muted); margin-bottom:0.75rem; font-weight:700; text-transform:uppercase;">Headers</div>
                <div style="color:var(--text-dark); background:var(--bg-main); padding:1rem; border-radius:8px;">Content-Type: application/json<br>User-Agent: MockClient/1.0</div>
                <div style="color:var(--text-muted); margin:1.5rem 0 0.75rem; font-weight:700; text-transform:uppercase;">Payload Body</div>
                <div style="color:var(--success); background:var(--bg-main); padding:1rem; border-radius:8px; white-space:pre-wrap;">{\n  "event": "user.created",\n  "id": "usr_${id}",\n  "timestamp": ${id}\n}</div>
            </div>
        `;
    };
    list.appendChild(div);
    div.click();
    showToast('Mock Webhook Received');
}

// --- ENVIRONMENTS ---
function renderEnvs() {
    const ws = DB.workspaces.find(x => x.id === DB.activeWs);
    const list = document.getElementById('env-list');
    list.innerHTML = '';
    if(!ws.envs || ws.envs.length===0) {
        list.innerHTML = `<div class="text-muted" style="text-align:center; padding:2rem;">No environment variables defined.</div>`;
        return;
    }
    ws.envs.forEach((e, idx) => {
        list.innerHTML += `
        <div class="flex gap-2 mb-2" style="margin-bottom:0.75rem; align-items:center;">
            <input type="text" class="input mono" value="${e.key}" style="flex:0.4;" readonly>
            <input type="password" class="input mono" value="${e.value}" style="flex:0.6;" id="env-v-${idx}">
            <button class="btn" onclick="const i=document.getElementById('env-v-${idx}'); i.type=i.type==='password'?'text':'password';"><i data-lucide="eye"></i></button>
            <button class="btn btn-danger" onclick="DB.workspaces.find(x=>x.id==DB.activeWs).envs.splice(${idx},1); saveDB(); renderEnvs();"><i data-lucide="trash"></i></button>
        </div>`;
    });
    lucide.createIcons();
}
function addEnv() {
    const ws = DB.workspaces.find(x => x.id === DB.activeWs);
    if(!ws.envs) ws.envs = [];
    ws.envs.push({ key: 'NEW_VAR_'+ws.envs.length, value: 'value' });
    saveDB(); renderEnvs();
}

// --- JSON TOOLS ---
function formatJson() {
    try {
        const val = JSON.parse(document.getElementById('json-input').value);
        document.getElementById('json-output').innerText = JSON.stringify(val, null, 2);
        showToast('Valid JSON Formatted');
        logHistory('Formatted JSON');
    } catch(e) {
        showToast('Invalid JSON syntax', 'error');
    }
}
function minifyJson() {
    try {
        const val = JSON.parse(document.getElementById('json-input').value);
        document.getElementById('json-output').innerText = JSON.stringify(val);
        showToast('JSON Minified');
    } catch(e) {
        showToast('Invalid JSON syntax', 'error');
    }
}

// --- HASH & DIFF ---
async function generateHash() {
    const text = document.getElementById('hash-input').value;
    const algo = document.getElementById('hash-algo').value;
    const msgUint8 = new TextEncoder().encode(text);
    const hashBuffer = await crypto.subtle.digest(algo, msgUint8);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    document.getElementById('hash-output').value = hashHex;
    logHistory(`Generated ${algo} Hash`);
}
function copyHash() {
    navigator.clipboard.writeText(document.getElementById('hash-output').value);
    showToast('Hash copied to clipboard');
}
function runDiff() {
    const t1 = document.getElementById('diff-1').value.split('\n');
    const t2 = document.getElementById('diff-2').value.split('\n');
    let res = '';
    const max = Math.max(t1.length, t2.length);
    for(let i=0; i<max; i++) {
        if(t1[i] !== t2[i]) {
            if(t1[i]!==undefined) res += `<div style="color:var(--error); background:rgba(228,117,155,0.1); padding:0.2rem 0.5rem; margin:1px 0;">- ${t1[i]}</div>`;
            if(t2[i]!==undefined) res += `<div style="color:var(--success); background:rgba(110,174,117,0.1); padding:0.2rem 0.5rem; margin:1px 0;">+ ${t2[i]}</div>`;
        } else {
            res += `<div style="color:var(--text-muted); padding:0.2rem 0.5rem;">  ${t1[i]}</div>`;
        }
    }
    document.getElementById('diff-result').innerHTML = res;
    logHistory('Ran Diff Checker');
}

// --- SNIPPETS & NOTES ---
function renderSnippets() {
    const list = document.getElementById('snippet-list');
    list.innerHTML = `<div class="ide-tabbar"><div class="ide-tab active">All Notes</div></div>`;
    if(DB.notes.length === 0) list.innerHTML += `<div class="p-4 text-muted" style="text-align:center; font-size:0.85rem;">No notes saved.</div>`;
    DB.notes.forEach(n => {
        list.innerHTML += `<div class="list-item" onclick="openSnippet('${n.id}')"><h4>${n.title}</h4></div>`;
    });
}
function createSnippet() {
    const title = prompt("Note Title:");
    if(!title) return;
    const note = { id: 'n'+Date.now(), title, content: 'Write your notes here...' };
    DB.notes.push(note); saveDB(); renderSnippets(); openSnippet(note.id);
}
function openSnippet(id) {
    const n = DB.notes.find(x => x.id === id);
    document.getElementById('snippet-editor').innerHTML = `
        <div class="ide-toolbar justify-between">
            <span style="font-weight:600; font-size:1rem;">${n.title}</span>
            <div><button class="btn btn-primary" onclick="saveSnippet('${id}')">Save</button> <button class="btn btn-danger" onclick="deleteSnippet('${id}')">Delete</button></div>
        </div>
        <textarea class="ide-editor" id="edit-snip-${id}">${n.content}</textarea>
    `;
}
function saveSnippet(id) {
    const n = DB.notes.find(x => x.id === id);
    n.content = document.getElementById(`edit-snip-${id}`).value;
    saveDB(); showToast('Note saved'); logHistory(`Edited Note: ${n.title}`);
}
function deleteSnippet(id) {
    DB.notes = DB.notes.filter(x => x.id !== id);
    saveDB(); renderSnippets(); document.getElementById('snippet-editor').innerHTML = '<div class="empty-state">Select a note</div>';
}

// --- HISTORY ---
function logHistory(action) {
    DB.history.unshift({ action, time: Date.now() });
    if(DB.history.length > 50) DB.history.pop();
    saveDB();
}
function renderHistory() {
    const tl = document.getElementById('timeline-list');
    tl.innerHTML = '';
    if(DB.history.length === 0) { tl.innerHTML = "No history available."; return; }
    DB.history.forEach(h => {
        tl.innerHTML += `
        <div class="t-item">
            <div class="t-dot"></div>
            <div class="t-time">${new Date(h.time).toLocaleTimeString()}</div>
            <div class="t-content">${h.action}</div>
        </div>`;
    });
}

// --- EXPORT ---
function exportData() {
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(DB, null, 2));
    const dlAnchorElem = document.createElement('a');
    dlAnchorElem.setAttribute("href", dataStr);
    dlAnchorElem.setAttribute("download", "devkit_export.json");
    dlAnchorElem.click();
    showToast("Data exported successfully");
}
function clearData() {
    if(confirm("Are you sure you want to delete ALL DevKit data?")) {
        localStorage.removeItem('devkit_db');
        location.reload();
    }
}

// Init
window.onload = () => {
    lucide.createIcons();
    parseCron();
};
