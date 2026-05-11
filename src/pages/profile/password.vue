<template>
  <view class="page">
    <view class="header">
      <text class="back" @click="goBack">‹</text>
      <text class="title">修改密码</text>
    </view>
    <view class="form">
      <view class="form-item">
        <text class="form-label">原密码</text>
        <input 
          class="form-input" 
          type="password" 
          v-model="oldPassword" 
          placeholder="请输入原密码"
        />
      </view>
      <view class="form-item">
        <text class="form-label">新密码</text>
        <input 
          class="form-input" 
          type="password" 
          v-model="newPassword" 
          placeholder="请输入新密码"
        />
      </view>
      <view class="form-item">
        <text class="form-label">确认密码</text>
        <input 
          class="form-input" 
          type="password" 
          v-model="confirmPassword" 
          placeholder="请确认新密码"
        />
      </view>
      <button class="submit-btn" @click="submit">保存修改</button>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { oldPassword: '', newPassword: '', confirmPassword: '' }
  },
  onShow() {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    async submit() {
      if (!this.oldPassword) {
        uni.showToast({ title: '请输入原密码', icon: 'none' })
        return
      }
      if (!this.newPassword) {
        uni.showToast({ title: '请输入新密码', icon: 'none' })
        return
      }
      if (this.newPassword !== this.confirmPassword) {
        uni.showToast({ title: '两次输入的密码不一致', icon: 'none' })
        return
      }
      try {
        await request({
          url: '/app/me/password',
          method: 'POST',
          data: {
            oldPassword: this.oldPassword,
            newPassword: this.newPassword,
          },
        })
        uni.showToast({ title: '修改成功', icon: 'success' })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        uni.showToast({ title: e.message || '修改失败', icon: 'none' })
      }
    },
  },
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f5f5;
}
.header {
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #1989fa;
}
.back {
  font-size: 48rpx;
  color: #fff;
  margin-right: 16rpx;
}
.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
}
.form {
  padding: 32rpx 24rpx;
}
.form-item {
  background: #fff;
  padding: 24rpx;
  margin-bottom: 16rpx;
  border-radius: 12rpx;
}
.form-label {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
  display: block;
}
.form-input {
  font-size: 28rpx;
  padding: 12rpx 0;
  border-bottom: 1px solid #f0f0f0;
}
.submit-btn {
  margin-top: 32rpx;
  background: #1989fa;
  color: #fff;
  border-radius: 44rpx;
}
</style>
