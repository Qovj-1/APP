const API_PREFIX = '/api'

export function getToken() {
  try {
    return uni.getStorageSync('adminToken') || uni.getStorageSync('app_token') || ''
  } catch (e) {
    return ''
  }
}

export function setAuth(token, account, name) {
  uni.setStorageSync('app_token', token || '')
  uni.setStorageSync('app_account', account || '')
  uni.setStorageSync('app_name', name || '')
}

export function clearAuth() {
  uni.removeStorageSync('app_token')
  uni.removeStorageSync('app_account')
  uni.removeStorageSync('app_name')
}

export function request({ url, method = 'GET', data }) {
  const token = getToken()
  return new Promise((resolve, reject) => {
    uni.request({
      url: API_PREFIX + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: 'Bearer ' + token } : {}),
      },
      success: (res) => {
        const body = res.data
        if (res.statusCode === 401) {
          clearAuth()
          reject(new Error('未登录'))
          return
        }
        if (body && body.code === 0) {
          resolve(body.data)
          return
        }
        reject(new Error((body && body.message) || '请求失败'))
      },
      fail: (err) => reject(err),
    })
  })
}
