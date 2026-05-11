/** 与 Spring Boot 管理端约定前缀；开发环境可走 Vite proxy */
const base = () => import.meta.env.VITE_API_BASE ?? '/api/admin'

export interface ApiEnvelope<T> {
  code: number
  message: string
  data: T
}

export async function adminFetch<T>(
  path: string,
  init?: RequestInit & { json?: unknown },
): Promise<T> {
  const url = `${base()}${path.startsWith('/') ? path : `/${path}`}`
  const headers = new Headers(init?.headers)
  const token = localStorage.getItem('nutritionist_admin_token')
  if (token) headers.set('Authorization', `Bearer ${token}`)
  if (init?.json !== undefined) {
    headers.set('Content-Type', 'application/json')
  }
  const res = await fetch(url, {
    ...init,
    headers,
    body: init?.json !== undefined ? JSON.stringify(init.json) : init?.body,
  })
  const ct = res.headers.get('content-type') ?? ''
  if (!ct.includes('application/json')) {
    const text = await res.text().catch(() => '')
    if (!res.ok) throw new Error(text || `请求失败：${res.status}`)
    return undefined as T
  }
  const raw = (await res.json()) as ApiEnvelope<T>
  if (raw && typeof raw.code === 'number' && raw.code !== 0) {
    throw new Error(raw.message || '请求失败')
  }
  if (!res.ok) {
    throw new Error(raw?.message || `请求失败：${res.status}`)
  }
  return raw.data as T
}
