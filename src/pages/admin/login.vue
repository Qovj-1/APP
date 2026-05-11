<template>
  <view class="login-page">
    <view class="bg-decoration">
      <view class="deco-circle deco-circle-1"></view>
      <view class="deco-circle deco-circle-2"></view>
      <view class="deco-circle deco-circle-3"></view>
    </view>
    
    <view class="login-container">
      <view class="logo-section">
        <view class="logo-wrapper">
          <image 
            class="logo-img" 
            src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20nutritionist%20health%20food%20icon%20with%20vegetables%20and%20fruits%20minimalist%20design&image_size=square"
            mode="aspectFit"
          />
        </view>
        <text class="system-title">健康营养师管理系统</text>
        <text class="system-subtitle">专业营养管理平台</text>
      </view>
      
      <view class="login-form">
        <view class="form-item">
          <view class="input-icon">
            <text class="icon-text">👤</text>
          </view>
          <input 
            class="form-input" 
            v-model="form.account" 
            placeholder="账号"
          />
        </view>
        <view class="form-item">
          <view class="input-icon">
            <text class="icon-text">🔒</text>
          </view>
          <input 
            class="form-input" 
            type="password" 
            v-model="form.password" 
            placeholder="密码"
          />
        </view>
        <button class="login-btn" @click="login" :loading="loading">
          <text class="btn-text">登 录</text>
        </button>
      </view>
      
      <view class="footer-tips">
        <text class="tips-text">© 2026 健康营养师管理系统</text>
      </view>
    </view>
  </view>
</template>

<script>
import { request } from '@/utils/api.js'

export default {
  data() {
    return {
      form: { account: '', password: '' },
      loading: false
    }
  },
  methods: {
    async login() {
      if (!this.form.account || !this.form.password) {
        uni.showToast({ title: '请输入账号密码', icon: 'none' })
        return
      }
      
      try {
        this.loading = true
        const res = await request({
          url: '/admin/auth/login',
          method: 'POST',
          data: {
            account: this.form.account,
            password: this.form.password
          }
        })
        
        uni.setStorageSync('adminToken', res.token)
        uni.setStorageSync('adminInfo', JSON.stringify(res))
        uni.redirectTo({ url: '/pages/admin/index' })
      } catch (e) {
        uni.showToast({ title: e.message || '登录失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 50%, #43e97b 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.bg-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
}

.deco-circle-1 {
  width: 600rpx;
  height: 600rpx;
  background: #43e97b;
  top: -100rpx;
  left: -100rpx;
}

.deco-circle-2 {
  width: 400rpx;
  height: 400rpx;
  background: #4facfe;
  bottom: -50rpx;
  right: -50rpx;
}

.deco-circle-3 {
  width: 300rpx;
  height: 300rpx;
  background: #00f2fe;
  top: 50%;
  right: 10%;
}

.login-container {
  width: 680rpx;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 32rpx;
  padding: 60rpx;
  box-shadow: 0 20rpx 80rpx rgba(0, 0, 0, 0.15);
  position: relative;
  z-index: 10;
}

.logo-section {
  text-align: center;
  margin-bottom: 56rpx;
}

.logo-wrapper {
  width: 160rpx;
  height: 160rpx;
  margin: 0 auto 32rpx;
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10rpx 40rpx rgba(67, 233, 123, 0.4);
}

.logo-img {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
}

.system-title {
  display: block;
  font-size: 40rpx;
  font-weight: 700;
  color: #2d3748;
  margin-bottom: 12rpx;
}

.system-subtitle {
  display: block;
  font-size: 26rpx;
  color: #718096;
}

.login-form {
  padding: 0 32rpx;
}

.form-item {
  margin-bottom: 28rpx;
  display: flex;
  align-items: center;
  background: #f7fafc;
  border-radius: 16rpx;
  border: 2rpx solid #e2e8f0;
  transition: all 0.3s ease;
}

.form-item:focus-within {
  border-color: #43e97b;
  box-shadow: 0 0 0 6rpx rgba(67, 233, 123, 0.1);
}

.input-icon {
  width: 80rpx;
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-text {
  font-size: 32rpx;
}

.form-input {
  flex: 1;
  height: 88rpx;
  padding: 0 20rpx;
  font-size: 30rpx;
  background: transparent;
  border: none;
}

.login-btn {
  width: 100%;
  height: 96rpx;
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: #fff;
  font-size: 34rpx;
  font-weight: 600;
  border-radius: 48rpx;
  margin-top: 40rpx;
  box-shadow: 0 10rpx 40rpx rgba(67, 233, 123, 0.4);
  transition: all 0.3s ease;
  border: none;
}

.login-btn:active {
  transform: scale(0.98);
  box-shadow: 0 5rpx 20rpx rgba(67, 233, 123, 0.3);
}

.btn-text {
  letter-spacing: 8rpx;
}

.footer-tips {
  text-align: center;
  margin-top: 48rpx;
  padding-top: 32rpx;
  border-top: 1rpx solid #e2e8f0;
}

.tips-text {
  font-size: 22rpx;
  color: #a0aec0;
}
</style>